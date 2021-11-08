package com.personalproject.todo.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    public static Map<String, Object> objectToMap(Class<?> cls, Object obj) {
        Map<String, Object> objectMap = new HashMap<>();
        Method[] methods = cls.getMethods();
        try {
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equals("getClass") || !methodName.startsWith("get")) continue;
                methodName = methodName.substring(3);
                methodName = methodName.replaceFirst(methodName.substring(0, 1), methodName.substring(0, 1).toLowerCase());
                objectMap.put(methodName, method.invoke(obj, new Object[] {}));
            }
        } catch (IllegalAccessException iae) {

        } catch (InvocationTargetException ite) {

        }
        return objectMap;
    }
}
