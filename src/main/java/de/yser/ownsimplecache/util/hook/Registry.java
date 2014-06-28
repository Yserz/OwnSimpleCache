package de.yser.ownsimplecache.util.hook;

import java.util.*;

/**
 *
 * @author Michael Koppen
 */
public class Registry {

	private static Map<Class<?>, Set<Object>> services = new HashMap<>();

	public static <T> void registerClass(Class<T> superClass, Class<? extends Object> implementor) throws Exception {
		if (implementor.isAnnotation()
			|| implementor.isAnonymousClass()
			|| implementor.isArray()
			|| implementor.isEnum()
			|| implementor.isInterface()
			|| implementor.isPrimitive()
			|| implementor.isSynthetic()) {
			throw new Exception("Given implementator-class is not an instanceable class!");
		}
		Set<Object> implementors = services.get(superClass);
		if (implementors != null) {
			implementors.add((Object) implementor.newInstance());
		} else {
			services.put(superClass, new HashSet<>(Arrays.asList(new Object[]{implementor.newInstance()})));
		}

	}

	public static <T> Set<T> getClasses(Class<T> superClass) {
		Set<Object> implementors = services.get(superClass);
		if (implementors != null) {
			return (Set<T>) implementors;
		}
		return new HashSet<>();
	}
}
