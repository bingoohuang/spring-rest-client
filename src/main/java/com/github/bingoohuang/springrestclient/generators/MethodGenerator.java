package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.utils.UniRestUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PathVariable;
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

    public MethodGenerator(Method method, ClassWriter classWriter) {
        this.method = method;
        this.mv = classWriter.visitMethod(ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
        this.annotations = method.getParameterAnnotations();
        this.paramSize = annotations.length;
        returnType = method.getReturnType();
    }

    public void genereate() {
        start();
        body();
        end();
    }


    private void body() {
        createMap(1, PathVariable.class);
        createMap(2, RequestParam.class);

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String requestMappingName = requestMapping.value()[0];
        mv.visitLdcInsn(requestMappingName);
        mv.visitVarInsn(ALOAD, paramSize + 1);
        mv.visitVarInsn(ALOAD, paramSize + 2);

        mv.visitLdcInsn(Type.getType(returnType));
        mv.visitMethodInsn(INVOKESTATIC, p(UniRestUtils.class), "asJson",
                sig(Object.class, String.class, Map.class, Map.class, Class.class), false);
        mv.visitTypeInsn(CHECKCAST, p(returnType));
        mv.visitInsn(ARETURN);
        mv.visitMaxs(-1, -1);
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
                mv.visitVarInsn(ALOAD, i + 1);
                mv.visitMethodInsn(INVOKEVIRTUAL, p(LinkedHashMap.class), "put",
                        sig(Object.class, Object.class, Object.class), false);
                mv.visitInsn(POP);
            }
        }
    }


    private void start() {
        mv.visitCode();
    }

    private void end() {
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }
}
