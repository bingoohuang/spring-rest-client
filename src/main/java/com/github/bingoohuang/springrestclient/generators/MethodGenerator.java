package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.BasicAuth;
import com.github.bingoohuang.springrestclient.annotations.SuccInResponseJSONProperty;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.BasicAuthProvider;
import com.github.bingoohuang.springrestclient.provider.SignProvider;
import com.github.bingoohuang.springrestclient.utils.*;
import com.google.common.primitives.Primitives;
import lombok.val;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.bingoohuang.asmvalidator.AsmParamsValidatorFactory.createValidatorSignature;
import static com.github.bingoohuang.asmvalidator.AsmParamsValidatorFactory.createValidators;
import static com.github.bingoohuang.springrestclient.utils.Asms.*;
import static com.github.bingoohuang.springrestclient.utils.PrimitiveWrappers.getParseXxMethodName;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.objectweb.asm.Opcodes.*;

public class MethodGenerator {
    public static final String StatusExceptionMappings = "StatusExceptionMappings";
    public static final String FixedRequestParams = "FixedRequestParams";
    public static final String SuccInResponseJSONProperty = "SuccInResponseJSONProperty";
    public static final String baseUrlProvider = "baseUrlProvider";
    public static final String basicAuthProvider = "basicAuthProvider";
    public static final String signProvider = "signProvider";
    public static final String appContext = "appContext";

    private final Method method;
    private final MethodVisitor mv;
    private final Annotation[][] annotations;
    private final int paramSize;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;
    private final int offsetSize;
    private final String classRequestMapping;
    private final RequestMapping requestMapping;
    private final boolean futureReturnType;
    private final boolean isBinaryReturnType;
    private final boolean isFutureBinaryReturnType;

    private final String implp;
    String restReqBuilder = p(RestReqBuilder.class);
    private String methodValidatorSignature;
    private boolean validatorsEnabled;


    public MethodGenerator(ClassWriter classWriter, String implName, Method method, String classRequestMapping) {
        this.implp = p(implName);
        this.method = method;
        this.mv = visitMethod(method, classWriter);
        this.annotations = method.getParameterAnnotations();
        this.parameterTypes = method.getParameterTypes();
        this.paramSize = annotations.length;
        this.offsetSize = computeOffsetSize();
        returnType = method.getReturnType();
        this.classRequestMapping = classRequestMapping;
        this.requestMapping = method.getAnnotation(RequestMapping.class);
        this.futureReturnType = Types.isFutureReturnType(method);
        this.isBinaryReturnType = returnType == InputStream.class;
        this.isFutureBinaryReturnType = futureReturnType
            && Types.getGenericTypeArgument(method) == InputStream.class;
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
        prepare();
        start();
        body();
        end();
    }

    private void prepare() {
        prepareMethodParametersValidators();
    }

    private void prepareMethodParametersValidators() {
        this.validatorsEnabled = createValidators(method);
        this.methodValidatorSignature = createValidatorSignature(method);
    }

    private void body() {
        generateValidateCode();
        createMap(1, PathVariable.class);
        createMap(2, RequestParam.class);
        createMap(3, CookieValue.class);

        buildUniRestReq();
        request();
        dealResult();
    }

    private void generateValidateCode() {
        if (paramSize == 0) return;
        if (!validatorsEnabled) return;

        mv.visitLdcInsn(Type.getType(method.getDeclaringClass()));
        mv.visitLdcInsn(methodValidatorSignature);
        if (paramSize <= 5) mv.visitInsn(ICONST_0 + paramSize);
        else mv.visitIntInsn(BIPUSH, paramSize);
        mv.visitTypeInsn(ANEWARRAY, p(Object.class));

        for (int i = 0; i < paramSize; ++i) {
            mv.visitInsn(DUP);
            if (i <= 5) mv.visitInsn(ICONST_0 + i);
            else mv.visitIntInsn(BIPUSH, i);
            mv.visitVarInsn(ALOAD, i + 1);
            mv.visitInsn(AASTORE);
        }

        mv.visitMethodInsn(INVOKESTATIC,
            p(AsmValidatorLog.class), "validate",
            sig(void.class, Class.class, String.class, Object[].class), false);
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
        mv.visitVarInsn(ASTORE, offsetSize + 4);
        mv.visitVarInsn(ALOAD, offsetSize + 4);

        if (isPostMethodOrNone()) {
            int requestBodyOffset = findRequestBodyParameterOffset();
            if (requestBodyOffset > -1) {
                mv.visitVarInsn(ALOAD, requestBodyOffset + 1);
                getOrPost(futureReturnType,
                    "postBodyAsync", sig(Future.class, Object.class),
                    "postBodyAsyncBinary", sig(Future.class, Object.class),
                    "postBody", sig(String.class, Object.class),
                    "postBodyBinary", sig(InputStream.class, Object.class));
            } else {
                getOrPost(futureReturnType,
                    "postAsync", sig(Future.class),
                    "postAsyncBinary", sig(Future.class),
                    "post", sig(String.class),
                    "postBinary", sig(InputStream.class));
            }
        } else if (isGetMethod()) {
            getOrPost(futureReturnType,
                "getAsync", sig(Future.class),
                "getAsyncBinary", sig(Future.class),
                "get", sig(String.class),
                "getBinary", sig(InputStream.class));
        }
    }

    private void getOrPost(boolean futureReturnType,
                           String async, String asyncSig,
                           String asyncBinary, String asyncSigBinary,
                           String sync, String syncSig,
                           String syncBinary, String syncSigBinary) {
        if (futureReturnType) {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), async, asyncSig, false);
        } else if (isBinaryReturnType) {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), syncBinary, syncSigBinary, false);
        } else if (isFutureBinaryReturnType) {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), asyncBinary, asyncSigBinary, false);
        } else {
            mv.visitMethodInsn(INVOKEVIRTUAL, p(RestReq.class), sync, syncSig, false);
        }
    }

    private void buildUniRestReq() {
        newObject(restReqBuilder);

        mv.visitLdcInsn(getFullRequestMapping());
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "prefix", sigRest(String.class), false);
        mv.visitLdcInsn(getFirstConsume());
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "firstConsume", sigRest(String.class), false);
        mv.visitInsn(futureReturnType ? ICONST_1 : ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "async", sigRest(boolean.class), false);
        mv.visitLdcInsn(Type.getType(method.getDeclaringClass()));
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "apiClass", sigRest(Class.class), false);

        setField(appContext, ApplicationContext.class);
        setField(baseUrlProvider, BaseUrlProvider.class);
        if (method.getDeclaringClass().isAnnotationPresent(BasicAuth.class)) {
            setField(basicAuthProvider, BasicAuthProvider.class);
        }
        setField(signProvider, SignProvider.class);
        setFieldPerMethod(SuccInResponseJSONProperty, SuccInResponseJSONProperty.class);
        setFieldPerMethod(StatusExceptionMappings, Map.class);
        setFieldPerMethod(FixedRequestParams, Map.class);

        mv.visitVarInsn(ALOAD, offsetSize + 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "routeParams", sigRest(Map.class), false);
        mv.visitVarInsn(ALOAD, offsetSize + 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "requestParams", sigRest(Map.class), false);
        mv.visitVarInsn(ALOAD, offsetSize + 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "cookies", sigRest(Map.class), false);

        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder, "build", sig(RestReq.class), false);
    }

    private void newObject(String objectClassPath) {
        mv.visitTypeInsn(NEW, objectClassPath);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, objectClassPath, "<init>", "()V", false);
    }

    private void setFieldPerMethod(String namePostFix, Class propertyClass) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, implp,
            method.getName() + namePostFix, ci(propertyClass));
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder,
            uncapitalize(namePostFix), sigRest(propertyClass), false);
    }

    private void setField(String buildMethodName, Class propertyClass) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, implp, buildMethodName, ci(propertyClass));
        mv.visitMethodInsn(INVOKEVIRTUAL, restReqBuilder,
            buildMethodName, sigRest(propertyClass), false);
    }

    private String sigRest(Class<?> clazz) {
        return sig(RestReqBuilder.class, clazz);
    }


    private String getFirstConsume() {
        val isEmpty = requestMapping == null || requestMapping.consumes().length == 0;
        if (isEmpty) return "";

        return requestMapping.consumes()[0];
    }

    private String getFullRequestMapping() {
        val isNotEmpty = requestMapping != null && requestMapping.value().length > 0;
        val methodMappingName = isNotEmpty ? requestMapping.value()[0] : "";

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

        val method = requestMapping.method();
        return method.length == 1 && method[0] == RequestMethod.GET;
    }

    private boolean isPostMethodOrNone() {
        if (requestMapping == null) return true;

        val method = requestMapping.method();
        if (method.length == 0) return true;

        return method.length == 1 && method[0] == RequestMethod.POST;
    }

    private void objectValueOfAndReturn() {
        if (returnType == String.class || returnType == Object.class || isBinaryReturnType) {
            mv.visitInsn(ARETURN);
            return;
        }

        if (futureReturnType) {
            val futureType = Types.getFutureGenericArgClass(method);
            if (!(futureType instanceof Class)) {
                mv.visitInsn(ARETURN);
                return;
            }

            mv.visitLdcInsn(Type.getType((Class) futureType));
            mv.visitVarInsn(ALOAD, offsetSize + 4);
            mv.visitMethodInsn(INVOKESTATIC, p(Futures.class),
                futureType == Void.class ? "convertFutureVoid" : "convertFuture",
                sig(Future.class, Future.class, Class.class, RestReq.class), false);
        } else {
            java.lang.reflect.Type typeArgument = Types.getGenericTypeArgument(method);
            if (typeArgument == null) {
                mv.visitLdcInsn(Type.getType(returnType));
                mv.visitMethodInsn(INVOKESTATIC, p(Beans.class), "unmarshal",
                    sig(Object.class, String.class, Class.class), false);
            } else {
                val genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedTypeImpl) {
                    buildGenericReturn((ParameterizedTypeImpl) genericReturnType);

                } else {
                    mv.visitLdcInsn(Type.getType(returnType));
                }

                mv.visitMethodInsn(INVOKESTATIC, p(Beans.class), "unmarshal",
                    sig(Object.class, String.class, java.lang.reflect.Type.class), false);
            }
        }
        mv.visitTypeInsn(CHECKCAST, p(returnType));
        mv.visitInsn(ARETURN);
    }

    private void buildGenericReturn(ParameterizedTypeImpl impl) {
        // ParameterizedTypeImpl.make(List.class, new Class[]{String.class}, null);
        mv.visitLdcInsn(Type.getType(method.getReturnType()));

        val actualTypeArgs = impl.getActualTypeArguments();

        if (paramSize <= 5) mv.visitInsn(ICONST_0 + actualTypeArgs.length);
        else mv.visitIntInsn(BIPUSH, actualTypeArgs.length);

        mv.visitTypeInsn(ANEWARRAY, p(Class.class));

        for (int i = 0; i < actualTypeArgs.length; ++i) {
            mv.visitInsn(DUP);

            if (i <= 5) mv.visitInsn(ICONST_0 + i);
            else mv.visitIntInsn(BIPUSH, i);

            mv.visitLdcInsn(Type.getType((Class) actualTypeArgs[i]));
            mv.visitInsn(AASTORE);
        }

        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKESTATIC, p(ParameterizedTypeImpl.class),
            "make", sig(ParameterizedTypeImpl.class, Class.class,
                java.lang.reflect.Type[].class,
                java.lang.reflect.Type.class), false);
    }


    private void primitiveValueOfAndReturn() {
        val wrapped = Primitives.wrap(returnType);
        mv.visitMethodInsn(INVOKESTATIC, p(wrapped), getParseXxMethodName(returnType),
            sig(returnType, String.class), false);

        mv.visitInsn(Type.getType(returnType).getOpcode(IRETURN));
    }


    private <T extends Annotation> void createMap(int index, Class<T> annotationClass) {
        newObject(p(LinkedHashMap.class));
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
