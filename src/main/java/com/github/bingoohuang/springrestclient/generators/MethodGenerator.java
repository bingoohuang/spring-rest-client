package com.github.bingoohuang.springrestclient.generators;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.utils.UniRests;
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

import static com.github.bingoohuang.springrestclient.utils.Asms.*;
import static com.github.bingoohuang.springrestclient.utils.PrimitiveWrappers.getParseXxMethodName;
import static org.objectweb.asm.Opcodes.*;


public class MethodGenerator {
    public static final String STATUS_EXCEPTION_MAPPINGS = "StatusExceptionMappings";

    private final Method method;
    private final MethodVisitor mv;
    private final Annotation[][] annotations;
    private final int paramSize;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;
    private final int offsetSize;
    private final String classRequestMapping;
    private final RequestMapping requestMapping;

    public MethodGenerator(Method method, ClassWriter classWriter, String classRequestMapping) {
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

        String impl = p(method.getDeclaringClass()) + "Impl";

        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, impl, method.getName() + STATUS_EXCEPTION_MAPPINGS, ci(Map.class));

        mv.visitLdcInsn(Type.getType(method.getDeclaringClass()));
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, impl, "baseUrlProvider", ci(BaseUrlProvider.class));

        mv.visitLdcInsn(getFullRequestMapping());
        mv.visitVarInsn(ALOAD, offsetSize + 1);
        mv.visitVarInsn(ALOAD, offsetSize + 2);

        if (isPostMethodOrNone()) {
            int requestBodyOffset = findRequestBodyParameterOffset();
            if (requestBodyOffset > -1) {
                mv.visitVarInsn(ALOAD, requestBodyOffset + 1);
                mv.visitMethodInsn(INVOKESTATIC, p(UniRests.class), "postAsJson",
                        sig(String.class, Map.class, Class.class, BaseUrlProvider.class,
                                String.class, Map.class, Map.class, Object.class), false);
            } else {
                mv.visitMethodInsn(INVOKESTATIC, p(UniRests.class), "post",
                        sig(String.class, Map.class, Class.class, BaseUrlProvider.class,
                                String.class, Map.class, Map.class), false);
            }
        } else if (isGetMethod()) {
            mv.visitMethodInsn(INVOKESTATIC, p(UniRests.class), "get",
                    sig(String.class, Map.class, Class.class, BaseUrlProvider.class,
                            String.class, Map.class, Map.class), false);
        }

        if (returnType == void.class) {
            mv.visitInsn(RETURN);
            return;
        }

        mv.visitVarInsn(ASTORE, offsetSize + 3);
        mv.visitVarInsn(ALOAD, offsetSize + 3);

        if (returnType.isPrimitive()) {
            primitiveValueOfAndReturn();
        } else {
            objectValueOfAndReturn();
        }

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
        if (returnType != String.class) {
            mv.visitLdcInsn(Type.getType(returnType));
            mv.visitMethodInsn(INVOKESTATIC, p(JSON.class), "parseObject",
                    sig(Object.class, String.class, Class.class), false);
            mv.visitTypeInsn(CHECKCAST, p(returnType));
        }

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
