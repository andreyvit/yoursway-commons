package com.yoursway.utils;

public class JavaStackFrameUtils {
    
    public static String callerPackageOutside(Class<?> klass) {
        return packageName(callerClassOutside(klass));
    }
    
    public static String callerClassOutside(Class<?> klass) {
        return callerStackTraceElementOutside(klass).getClassName();
    }
    
    public static String callerMethodOutside(Class<?> klass) {
        return callerStackTraceElementOutside(klass).getMethodName();
    }
    
    public static StackTraceElement callerStackTraceElementOutside(Class<?> klass) {
        boolean thisClassMet = false;
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            boolean inThisClass = klass.getName().equals(element.getClassName());
            if (!thisClassMet) {
                if (inThisClass)
                    thisClassMet = true;
            } else {
                if (!inThisClass)
                    return element;
            }
        }
        throw new AssertionError("Unreachable");
    }
    
    public static String removeBasePackageName(String className, String packageName) {
        String prefix = packageName + ".";
        if (!className.startsWith(prefix))
            throw new IllegalArgumentException("Class " + className + " is not from package " + packageName);
        return className.substring(prefix.length());
    }
    
    public static String packageName(Class<?> klass) {
        return packageName(klass.getName());
    }
    
    public static String packageName(String className) {
        int pos = className.lastIndexOf('.');
        if (pos < 0)
            return "";
        return className.substring(0, pos);
    }
    
    public static boolean isTrivialExtention(String interfacePackage, String implementationPackage) {
        if (implementationPackage.equals(interfacePackage))
            return true;
        if (implementationPackage.equals(interfacePackage + ".impl")
                || implementationPackage.equals(interfacePackage + ".implementation"))
            return true;
        int internalPos = implementationPackage.indexOf(".internal.");
        if (internalPos >= 0 && interfacePackage.indexOf(".internal.") < 0
                && interfacePackage.equals(remove(implementationPackage, internalPos, ".internal".length())))
            return true;
        return false;
    }
    
    private static String remove(String s, int start, int length) {
        return s.substring(start) + s.substring(start + length);
    }
    
}
