package com.github.bingoohuang.springrestclient.generators;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.springrestclient.utils.AsmUtils;
import com.github.bingoohuang.springrestclient.utils.UniRestUtils;
import com.google.common.primitives.Primitives;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import static org.objectweb.asm.Opcodes.*;


public class MethodGenerator {
    private final Method method;
    private final MethodVisitor mv;
    private final Annotation[][] annotations;
    private final int paramSize;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;

    public MethodGenerator(Method method, ClassWriter classWriter) {
        this.method = method;
        this.mv = classWriter.visitMethod(ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
        this.annotations = method.getParameterAnnotations();
        this.parameterTypes = method.getParameterTypes();
        this.paramSize = annotations.length;
        returnType = method.getReturnType();
    }

    public void generate() {
        start();
        body();
        end();
    }


    private void body() {
        createMap(1, PathVariable.class);
        createMap(2, RequestParam.class);

        int requestBodyIndex = -1;
        for (int i = 0; i < paramSize; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == RequestBody.class) {
                    requestBodyIndex = i;
                    break;
                }
            }
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String requestMappingName = requestMapping.value()[0];
        mv.visitLdcInsn(requestMappingName);
        mv.visitVarInsn(ALOAD, paramSize + 1);
        mv.visitVarInsn(ALOAD, paramSize + 2);

        if (requestBodyIndex < 0) {
            mv.visitLdcInsn(Type.getType(returnType));
            mv.visitMethodInsn(INVOKESTATIC, p(UniRestUtils.class), "get",
                    sig(Object.class, String.class, Map.class, Map.class, Class.class), false);
            mv.visitTypeInsn(CHECKCAST, p(returnType));
            mv.visitInsn(ARETURN);
        } else {
            mv.visitVarInsn(ALOAD, requestBodyIndex + 1);
            mv.visitMethodInsn(INVOKESTATIC, p(UniRestUtils.class), "postAsJson",
                    sig(String.class, String.class, Map.class, Map.class, Object.class), false);
            mv.visitVarInsn(ASTORE, paramSize + 3);
            mv.visitVarInsn(ALOAD, paramSize + 3);

            if (returnType.isPrimitive()) {
                primitiveValueOfAndReturn();
            } else {
                objectValueOfAndReturn();
            }
        }
        mv.visitMaxs(-1, -1);
    }

    private void objectValueOfAndReturn() {
        mv.visitLdcInsn(Type.getType(returnType));
        mv.visitMethodInsn(INVOKESTATIC, p(JSON.class), "parseObject", sig(Object.class, String.class, Class.class), false);
        mv.visitTypeInsn(CHECKCAST, p(returnType));
        mv.visitInsn(ARETURN);
    }

    private void primitiveValueOfAndReturn() {
        Class<?> wrapped = Primitives.wrap(returnType);
        mv.visitMethodInsn(INVOKESTATIC, p(wrapped), "valueOf", sig(wrapped, String.class), false);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(wrapped), AsmUtils.getXxValueMethodName(returnType), sig(returnType), false);

        Type returnAsmType = Type.getType(returnType);
        mv.visitInsn(returnAsmType.getOpcode(IRETURN));
    }

    private <T extends Annotation> void createMap(int index, Class<T> annotationClass) {
        mv.visitTypeInsn(NEW, p(LinkedHashMap.class));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, p(LinkedHashMap.class), "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, paramSize + index);

        for (int i = 0; i < paramSize; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() != annotationClass) continue;

                mv.visitVarInsn(ALOAD, paramSize + index);
                String value = (String) AnnotationUtils.getValue(annotation);
                mv.visitLdcInsn(value);
                wrapPrimitive(parameterTypes[i], i);
                mv.visitMethodInsn(INVOKEVIRTUAL, p(LinkedHashMap.class), "put",
                        sig(Object.class, Object.class, Object.class), false);
                mv.visitInsn(POP);
            }
        }
    }

    private void wrapPrimitive(Class<?> type, int paramIndex) {
        Type parameterAsmType = Type.getType(type);
        int opcode = parameterAsmType.getOpcode(Opcodes.ILOAD);
        mv.visitVarInsn(opcode, paramIndex + 1);

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
