package com.github.bingoohuang.springrestclient.utils;

import lombok.val;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class Asms {
    // Creates a dotted class name from a path/package name
    public static String c(String p) {
        return p.replace('/', '.');
    }

    // Creates a class path name, from a Class.
    public static String p(Class n) {
        return n.getName().replace('.', '/');
    }

    public static String p(String className) {
        return className.replace('.', '/');
    }


    // Creates a class identifier of form Labc/abc;, from a Class.
    public static String ci(Class n) {
        if (n.isArray()) {
            n = n.getComponentType();
            if (n.isPrimitive()) {
                if (n == Byte.TYPE) return "[B";
                if (n == Boolean.TYPE) return "[Z";
                if (n == Short.TYPE) return "[S";
                if (n == Character.TYPE) return "[C";
                if (n == Integer.TYPE) return "[I";
                if (n == Float.TYPE) return "[F";
                if (n == Double.TYPE) return "[D";
                if (n == Long.TYPE) return "[J";
                throw new RuntimeException("Unrecognized type in compiler: " + n.getName());
            } else {
                return "[" + ci(n);
            }
        } else {
            if (n.isPrimitive()) {
                if (n == Byte.TYPE) return "B";
                if (n == Boolean.TYPE) return "Z";
                if (n == Short.TYPE) return "S";
                if (n == Character.TYPE) return "C";
                if (n == Integer.TYPE) return "I";
                if (n == Float.TYPE) return "F";
                if (n == Double.TYPE) return "D";
                if (n == Long.TYPE) return "J";
                if (n == Void.TYPE) return "V";
                throw new RuntimeException("Unrecognized type in compiler: " + n.getName());
            } else {
                return "L" + p(n) + ";";
            }
        }
    }

    // Create a method signature from the given param types and return values
    public static String sig(Class retval, Class... params) {
        return sigParams(params) + ci(retval);
    }

    public static String sig(Class[] retvalParams) {
        val justParams = new Class[retvalParams.length - 1];
        System.arraycopy(retvalParams, 1, justParams, 0, justParams.length);
        return sigParams(justParams) + ci(retvalParams[0]);
    }

    public static String sig(Class retval, String descriptor, Class... params) {
        return sigParams(descriptor, params) + ci(retval);
    }

    public static String sigParams(Class... params) {
        val signature = new StringBuilder("(");

        for (int i = 0; i < params.length; i++) {
            signature.append(ci(params[i]));
        }

        signature.append(")");

        return signature.toString();
    }

    public static String sigParams(String descriptor, Class... params) {
        val signature = new StringBuilder("(");
        signature.append(descriptor);

        for (int i = 0; i < params.length; i++) {
            signature.append(ci(params[i]));
        }

        signature.append(")");
        return signature.toString();
    }

    public static void startNewArray(MethodVisitor mv, int paramSize, Class<?> objectClass) {
        if (paramSize <= 5) mv.visitInsn(ICONST_0 + paramSize);
        else mv.visitIntInsn(BIPUSH, paramSize);
        mv.visitTypeInsn(ANEWARRAY, p(objectClass));
    }

    public static void addItemToArray(MethodVisitor mv, int i, int ind) {
        mv.visitInsn(DUP);

        if (i <= 5) mv.visitInsn(ICONST_0 + i);
        else mv.visitIntInsn(BIPUSH, i);

        mv.visitVarInsn(ALOAD, ind);
        mv.visitInsn(AASTORE);
    }

    public static void addItemToArray(MethodVisitor mv, int i, Class<?> clazz) {
        mv.visitInsn(DUP);

        if (i <= 5) mv.visitInsn(ICONST_0 + i);
        else mv.visitIntInsn(BIPUSH, i);

        mv.visitLdcInsn(Type.getType(clazz));
        mv.visitInsn(AASTORE);
    }
}
