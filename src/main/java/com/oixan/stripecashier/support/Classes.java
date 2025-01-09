package com.oixan.stripecashier.support;

import java.lang.reflect.Field;

import jakarta.persistence.Id;


/**
 * A utility class for working with classes and reflection.
 */
public class Classes {

    	
	/**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private Classes() {
		// Private constructor to prevent instantiation
	}

    /**
     * Finds and returns the field annotated with {@link Id} in the given class.
     *
     * <p>This method searches through the declared fields of the provided class and returns the first 
     * field that is annotated with the {@link Id} annotation. If no such field is found, a {@link NoSuchFieldException} 
     * is thrown.</p>
     *
     * @param clazz the {@link Class} object representing the class to search
     * @return the {@link Field} object representing the field annotated with {@link Id}
     * @throws NoSuchFieldException if no field annotated with {@link Id} is found in the class
     */
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
