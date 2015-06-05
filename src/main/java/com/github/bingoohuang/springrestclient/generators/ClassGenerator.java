package com.github.bingoohuang.springrestclient.generators;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.BaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.FixedBaseUrlProvider;
import com.github.bingoohuang.springrestclient.provider.NoneBaseUrlProvider;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

public class ClassGenerator<T> {
    private final Class<T> restClientClass;
    private final String implName;
    private final ClassWriter classWriter;
    private final SpringRestClientEnabled restClientEnabled;

    public ClassGenerator(Class<T> restClientClass) {
        this.restClientClass = restClientClass;
        this.implName = restClientClass.getName() + "Impl";
        this.classWriter = createClassWriter();
        this.restClientEnabled = restClientClass.getAnnotation(SpringRestClientEnabled.class);
    }

    public Class<? extends T> generate() {
        byte[] bytes = createImplClassBytes();

        createClassFileForDiagnose(bytes);

        return defineClass(bytes);
    }

    public String getClassRequestMapping() {
        RequestMapping re = restClientClass.getAnnotation(RequestMapping.class);
        return re != null && re.value().length > 0 ? re.value()[0] : "";
    }

    private void createClassFileForDiagnose(byte[] bytes) {
        if (restClientEnabled.createClassFileForDiagnose())
            writeClassFile4Diagnose(bytes, restClientClass.getSimpleName() + "Impl.class");
    }

    private void writeClassFile4Diagnose(byte[] bytes, String fileName) {
        try {
            Files.write(bytes, new File(fileName));
        } catch (IOException e) {
            // ignore
        }
    }

    private Class<? extends T> defineClass(byte[] bytes) {
        RestClientClassLoader classLoader = new RestClientClassLoader(restClientClass.getClassLoader());
        return (Class<? extends T>) classLoader.defineClass(implName, bytes);
    }

    private byte[] createImplClassBytes() {
        constructor();

        String classRequestMapping = getClassRequestMapping();

        for (Method method : restClientClass.getMethods()) {
            new MethodGenerator(method, classWriter, classRequestMapping).generate();
        }

        return createBytes();
    }


    private byte[] createBytes() {
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private ClassWriter createClassWriter() {
        final String implSourceName = implName.replace('.', '/');
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        String[] interfaces = {Type.getInternalName(restClientClass)};
        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, implSourceName, null, "java/lang/Object", interfaces);

        FieldVisitor fv = cw.visitField(0, "baseUrlProvider",
                Type.getType(BaseUrlProvider.class).getDescriptor(),
                null, null);
        fv.visitEnd();
        return cw;
    }

    private void constructor() {
        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
}
