package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/*
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        for (Class<?> clazz : Collections.class.getDeclaredClasses()) {
            if (List.class.isAssignableFrom(clazz) &&
                    Modifier.isPrivate(clazz.getModifiers()) &&
                    Modifier.isStatic(clazz.getModifiers())) {
                // получаем первый конструктор
                Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
                // получаем значения параметров по умолчанию в соответствии с их типами
                Object[] parameterValues = defaultValues(constructor.getParameterTypes());
                try {
                    // разрешаем доступ к конструктору и создаем объект класса
                    constructor.setAccessible(true);
                    Object obj = constructor.newInstance(parameterValues);
                    // получаем метод get с одним параметром
                    Method methodGet = clazz.getDeclaredMethod("get", int.class);
                    methodGet.setAccessible(true);
                    methodGet.invoke(obj, 10);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException ignored) {
                } catch (InvocationTargetException e) {
//                    if (e.getCause().toString().contains("IndexOutOfBoundsException"))
//                        return clazz;
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object[] defaultValues(Class<?>[] types) {
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (type.equals(char.class) || type.equals(Character.class))
                values[i] = '\u0000';
            else if (type.equals(int.class) || type.equals(Integer.class))
                // в классе CopiesList, если индекс больше значения переданного
                // в конструкторе, метод get бросает InvocationTargetException
                values[i] = 10;
            else if (type.equals(double.class) || type.equals(Double.class))
                values[i] = 0.0;
            else
                values[i] = new Object();
        }
        return values;
    }
}
