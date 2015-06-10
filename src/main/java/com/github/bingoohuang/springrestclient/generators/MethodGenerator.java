package com.github.bingoohuang.springrestclient.generators;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.utils.Futures;
import com.github.bingoohuang.springrestclient.utils.RestReq;
import com.github.bingoohuang.springrestclient.utils.RestReqBuilder;
import com.google.common.primitives.Primitives;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.bingoohuang.springrestclient.utils.Asms.*;
import static com.github.bingoohuang.springrestclient.utils.PrimitiveWrappers.getParseXxMethodName;
import static org.objectweb.asm.Opcodes.*;


public class MethodGenerator {
    public static final String StatusExceptionMappings = "StatusExceptionMappings";
    public static final String FixedRequestParams = "FixedRequestParams";
    public static final String SuccInResponseJSONProperty = "SuccInResponseJSONProperty";

    private final Method method;
    private final MethodVisitor mv;
    private final Annotation[][] annotations;
    private final int paramSize;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;
    private final int offsetSize;
    private final String classRequestMapping;
    private final RequestMapping requestMapping;
    private final String implName;

    public MethodGenerator(ClassWriter classWriter, String implName, Method method, String classRequestMapping) {
        this.implName = implName;
        this.method = method;
        this.mv = visitMethod(method, classWriter);
        this.annotations = method.getParameterAnnotations();
        this.parameterTypes = method.getParameterTypes();
        this.paramSize = annotations.length;
        this.offsetSize = computeOffsetSize();
        returnType = method.getReturnType();
        this.classRequestMapping = classRequestMapping;
        this.requestMapping = method.getAnnotation(RequestMapping.class);
    }

    private MethodVisitor visitMethod(Method method, ClassWriter classWriter) {
        String methodDescriptor = Type.getMethodDescriptor(method);
        return classWriter.visitMethod(ACC_PUBLIC, method.getName(), methodDescriptor, null, null);
    }

    private int computeOffsetSize() {
        int cnt = 0;
        for (Class<?> parameterType : parameterTypes) {
            if (isWideType(parameterType)) ++cnt;
        }

        return paramSize + cnt;
    }

    public void generate() {
        start();
        body();
        end();
    }

    private void body() {
        createMap(1, PathVariable.class);
        createMap(2, RequestParam.class);

        buildUniRestReq();

        request();

        dealResult();
    }

    private void dealResult() {
        if (returnType == void.class) {
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            return;
        }

        if (returnType.isPrimitive()) {
            primitiveValueOfAndReturn();
        } else {
            objectValueOfAndReturn();
        }
    }

    private void request() {
        mv.visitVarInsn(ASTORE, offsetSize + 3);
        mv.visitVarInsn(ALOAD, offsetSize + 3);

        boolean futureReturnType = Futures.isFutureReturnType(method);

        if (isPostMethodOrNone()) {
            int requestBodyOffset = findRequestBodyParameterOffset();
            if (requestBodyOffset > -1) {
                mv.visitVarInsn(ALOAD, requestBodyOffset + 1);
                getOrPost(futureReturnType, "postAsJsonAsync", sig(Future.class, Object.class),
                        "postAsJson", sig(String.class, Object.class));
            } else {
                getOrPost(futureReturnType, "postAsync", sig(Future.class), "post", sig(String.class));
            }
        } else if (isGetMethod()) {
            getOrPost(futureReturnType, "getAsync", sig(Future.class), "get", sig(String.class));
        }
    }

    private void getOrPost(boolean futureReturnType, String getAsync, String sig, String get, String sig2) {
        if (futureReturnType) {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), getAsync, sig, false);
        } else {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), get, sig2, false);
        }
    }

    private void buildUniRestReq() {
        String impl = p(implName);

        mv.visitTypeInsn(NEW, p(RestReqBuilder.class));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, p(RestReqBuilder.class), "<init>", "()V", false);
        mv.visitLdcInsn(getFullRequestMapping());
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "prefix",
                sig(RestReqBuilder.class, String.class), false);
        mv.visitLdcInsn(Type.getType(method.getDeclaringClass()));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "apiClass",
                sig(RestReqBuilder.class, Class.class), false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, impl, "baseUrlProvider", ci(BaseUrlProvider.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "baseUrlProvider",
                sig(RestReqBuilder.class, BaseUrlProvider.class), false);
        mv.visitVarInsn(ALOAD, 0);
        String methodName = method.getName();
        mv.visitFieldInsn(GETFIELD, impl, methodName + SuccInResponseJSONProperty,
                ci(SuccInResponseJSONProperty.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "succInResponseJSONProperty",
                sig(RestReqBuilder.class, SuccInResponseJSONProperty.class), false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, impl, methodName + StatusExceptionMappings, ci(Map.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "statusExceptionMappings",
                sig(RestReqBuilder.class, Map.class), false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, impl, methodName + FixedRequestParams, ci(Map.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "fixedRequestParams",
                sig(RestReqBuilder.class, Map.class), false);
        mv.visitVarInsn(ALOAD, offsetSize + 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "routeParams",
                sig(RestReqBuilder.class, Map.class), false);
        mv.visitVarInsn(ALOAD, offsetSize + 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "requestParams",
                sig(RestReqBuilder.class, Map.class), false);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReqBuilder.class), "build",
                sig(RestReq.class), false);
    }

    private String getFullRequestMapping() {
        boolean isEmpty = requestMapping != null && requestMapping.value().length > 0;
        String methodMappingName = isEmpty ? requestMapping.value()[0] : "";

        return classRequestMapping + methodMappingName;
    }

    private int findRequestBodyParameterOffset() {
        for (int i = 0, incr = 0; i < paramSize; i++) {
            if (isWideType(parameterTypes[i])) ++incr;

            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == RequestBody.class) {
                    return i + incr;
                }
            }
        }

        return -1;
    }

    private boolean isWideType(Class<?> parameterType) {
        return parameterType == long.class || parameterType == double.class;
    }

    private boolean isGetMethod() {
        if (requestMapping == null) return false;

        RequestMethod[] method = requestMapping.method();
        return method.length == 1 && method[0] == RequestMethod.GET;
    }

    private boolean isPostMethodOrNone() {
        if (requestMapping == null) return true;

        RequestMethod[] method = requestMapping.method();
        if (method.length == 0) return true;

        return method.length == 1 && method[0] == RequestMethod.POST;
    }

    private void objectValueOfAndReturn() {
        if (returnType == String.class || returnType == Object.class) {
            mv.visitInsn(ARETURN);
            return;
        }

        boolean futureReturnType = Futures.isFutureReturnType(method);
        if (futureReturnType) {
            java.lang.reflect.Type futureType = Futures.getFutureGenericArgClass(method);
            if (!(futureType instanceof Class)) {
                mv.visitInsn(ARETURN);
                return;
            }

            mv.visitLdcInsn(Type.getType((Class) futureType));
            mv.visitVarInsn(ALOAD, offsetSize + 3);
            mv.visitMethodInsn(INVOKESTATIC, p(Futures.class),
                    futureType == Void.class ? "convertFutureVoid" : "convertFuture",
                    sig(Future.class, Future.class, Class.class, RestReq.class), false);
        } else {
            mv.visitLdcInsn(Type.getType(returnType));
            mv.visitMethodInsn(INVOKESTATIC, p(JSON.class), "parseObject",
                    sig(Object.class, String.class, Class.class), false);

        }
        mv.visitTypeInsn(CHECKCAST, p(returnType));
        mv.visitInsn(ARETURN);
    }

    private void primitiveValueOfAndReturn() {
        Class<?> wrapped = Primitives.wrap(returnType);
        mv.visitMethodInsn(INVOKESTATIC, p(wrapped), getParseXxMethodName(returnType),
                sig(returnType, String.class), false);

        mv.visitInsn(Type.getType(returnType).getOpcode(IRETURN));
    }

    private <T extends Annotation> void createMap(int index, Class<T> annotationClass) {
        mv.visitTypeInsn(NEW, p(LinkedHashMap.class));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, p(LinkedHashMap.class), "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, offsetSize + index);

        for (int i = 0, incr = 0; i < paramSize; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() != annotationClass) continue;

                mv.visitVarInsn(ALOAD, offsetSize + index);
                mv.visitLdcInsn(AnnotationUtils.getValue(annotation));
                wrapPrimitive(parameterTypes[i], i, incr);

                if (isWideType(parameterTypes[i])) ++incr;

                mv.visitMethodInsn(INVOKEVIRTUAL, p(LinkedHashMap.class), "put",
                        sig(Object.class, Object.class, Object.class), false);
                mv.visitInsn(POP);
            }
        }
    }

    private void wrapPrimitive(Class<?> type, int paramIndex, int incr) {
        Type parameterAsmType = Type.getType(type);
        int opcode = parameterAsmType.getOpcode(Opcodes.ILOAD);
        mv.visitVarInsn(opcode, paramIndex + 1 + incr);

        if (!type.isPrimitive()) return;

        Class<?> wrapped = Primitives.wrap(type);
        mv.visitMethodInsn(INVOKESTATIC, p(wrapped), "valueOf", sig(wrapped, type), false);
    }

    private void start() {
        mv.visitCode();
    }

    private void end() {
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }
}
