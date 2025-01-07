package com.oixan.stripecashier.support;

import java.lang.reflect.Field;

import jakarta.persistence.Id;

public class Classes {

  public static Field findIdField(Class<?> clazz) throws NoSuchFieldException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true); 
                return field;
            }
        }
        throw new NoSuchFieldException("ID field not found in class " + clazz.getName());
    }
  
}
