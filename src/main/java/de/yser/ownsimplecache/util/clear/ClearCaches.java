package de.yser.ownsimplecache.util.clear;

import java.lang.annotation.*;
import javax.enterprise.util.Nonbinding;

/**
 *
 * @author MacYser
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClearCaches {

	@Nonbinding
	ClearCache[] value();
}
