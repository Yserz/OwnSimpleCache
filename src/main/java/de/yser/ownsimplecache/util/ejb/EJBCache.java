package de.yser.ownsimplecache.util.ejb;

import java.lang.annotation.*;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Michael Koppen
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EJBCache {

	/**
	 * If the static key is set the returnvalue of the annotated method will be
	 * cached with this key no matter if any parameters are given.
	 *
	 * @return The static key.
	 */
	@Nonbinding
	String staticKey() default "";

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
