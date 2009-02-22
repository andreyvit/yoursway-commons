package com.yoursway.utils;

import static com.google.common.collect.Maps.newIdentityHashMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class DebugOutputHelper {
    
    private static Object MARKER = Boolean.TRUE;
    
    private static ThreadLocal<Map<Object, Object>> objectsBeingPrinted = new ThreadLocal<Map<Object, Object>>();
    
    /**
     * Field values are not used � the only point of them is to avoid
     * "unused field" warnings.
     */
    public static String reflectionBasedToString(Object object, Object... fieldValues) {
        return reflectionBasedToString(object);   
    }
    
    public static String reflectionBasedToString(Object object) {
        if (isObjectBeingPrinted(object))
            return "...";
        startPrintingObject(object);
        try {
            Class<?> klass = object.getClass();
            int fieldCount = countFields(klass);
            String simpleName = YsDebugging.simpleNameOf(klass);
            if (fieldCount == 0)
                return simpleName;
            boolean showFieldNames = (fieldCount > 1);
            return showFields(object, klass, simpleName, showFieldNames);
        } finally {
            finishedPrintingObject(object);
        }
    }

    private static String showFields(Object object, Class<?> klass, String simpleName, boolean showFieldNames) {
        StringBuilder result = new StringBuilder();
        result.append(simpleName);
        boolean firstField = true;
        for (Field field : klass.getDeclaredFields()) {
            if (isStatic(field))
                continue;
            if (firstField) {
                result.append("(");
                firstField = false;
            } else {
                result.append(", ");
            }
            appendFieldWithValue(field, object, result, showFieldNames);
        }
        if (!firstField)
            result.append(")");
        return result.toString();
    }

    private static int countFields(Class<?> klass) {
        int fieldCount = 0;
        for (Class<?> currentClass = klass; currentClass != null; currentClass = currentClass.getSuperclass())
            for (Field field : klass.getDeclaredFields())
                if (!isStatic(field))
                    fieldCount++;
        return fieldCount;
    }

    private static boolean isStatic(Field field) {
        int modifiers = field.getModifiers();
        return (0 != (modifiers & Modifier.STATIC));
    }
    
    private static void appendFieldWithValue(Field field, Object object, StringBuilder result, boolean showFieldNames) {
        if (showFieldNames)
            result.append(field.getName()).append('=');
        try {
            Object value = field.get(object);
            result.append(value.toString());
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            result.append("<ERR>");
        }
    }
    
    private static boolean isObjectBeingPrinted(Object object) {
        return objectsBeingPrinted().containsKey(object);
    }
    
    private static void startPrintingObject(Object object) {
        objectsBeingPrinted().put(object, MARKER);
    }
    
    private static void finishedPrintingObject(Object object) {
        objectsBeingPrinted().remove(object);
    }
    
    private static Map<Object, Object> objectsBeingPrinted() {
        Map<Object, Object> map = objectsBeingPrinted.get();
        if (map == null) {
            map = newIdentityHashMap();
            objectsBeingPrinted.set(map);
        }
        return map;
    }
}
