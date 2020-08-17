package de.rub.bi.inf.baclient.core.utils;

public class ClassUtils {

	
	public static boolean isPrimitiveOrJavaLang(Class<?> c) {
		return (c.isPrimitive() 
				|| c.getPackage().getName().equals("java.lang")
				|| c.getPackage().getName().equals("java.math")
				);
	}
}
