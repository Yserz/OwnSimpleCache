package de.yser.ownsimplecache.util.clear;

import java.lang.annotation.*;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Michael Koppen
 */
@Inherited
@Repeatable(ClearCaches.class)
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClearCache {

	/**
	 * Defines which cache will be cleared when called.
	 *
	 * @return
	 */
	@Nonbinding
	String cacheType() default "";

	/**
	 * If this value is set to true all caches will be cleared when called.
	 *
	 * @return
	 */
	@Nonbinding
	boolean invalidateAllCaches() default false;

	/**
	 * The generic type hint will help the framework to determine the generic
	 * type of e.g. Lists. Because of type erasure of generic types in java this
	 * is a possibility to e.g. distinguish between two different Lists.
	 *
	 * @return The static key.
	 */
	@Nonbinding
	String genericTypeHint() default "";
}
