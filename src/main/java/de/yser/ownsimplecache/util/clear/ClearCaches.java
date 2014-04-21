package de.yser.ownsimplecache.util.clear;

import java.lang.annotation.*;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author MacYser
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClearCaches {

	@Nonbinding
	ClearCache[] value();
}
