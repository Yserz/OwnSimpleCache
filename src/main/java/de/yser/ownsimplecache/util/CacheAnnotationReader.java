package de.yser.ownsimplecache.util;

import de.yser.ownsimplecache.util.clear.ClearCache;
import de.yser.ownsimplecache.util.ejb.EJBCache;
import de.yser.ownsimplecache.util.jaxrs.RESTCache;
import java.lang.reflect.Method;

/**
 *
 * @author Michael Koppen
 */
public class CacheAnnotationReader {

	public String getGenericTypeHint(Method method) {
		String genericTypeHint = null;
		EJBCache ejbAnnotation = method.getAnnotation(EJBCache.class);
		RESTCache restAnnotation = method.getAnnotation(RESTCache.class);
		ClearCache clearAnnotation = method.getAnnotation(ClearCache.class);
		if (ejbAnnotation != null && !ejbAnnotation.genericTypeHint().equals("")) {
			genericTypeHint = ejbAnnotation.genericTypeHint();
		}
		if (restAnnotation != null && !restAnnotation.genericTypeHint().equals("")) {
			genericTypeHint = restAnnotation.genericTypeHint();
		}
		if (clearAnnotation != null && !clearAnnotation.genericTypeHint().equals("")) {
			genericTypeHint = clearAnnotation.genericTypeHint();
		}
		return genericTypeHint;
	}

	public String getCacheType(Method method) {
		String cacheType = null;
		ClearCache clearAnnotation = method.getAnnotation(ClearCache.class);

		if (clearAnnotation != null && !clearAnnotation.cacheType().equals("")) {
			cacheType = clearAnnotation.cacheType();
		}
		return cacheType;
	}

	public boolean getInvalidateAllCaches(Method method) {
		boolean invalidateAllCaches = false;
		ClearCache clearAnnotation = method.getAnnotation(ClearCache.class);

		if (clearAnnotation != null && clearAnnotation.invalidateAllCaches() != false) {
			invalidateAllCaches = clearAnnotation.invalidateAllCaches();
		}
		return invalidateAllCaches;
	}

	public String getStaticKey(Method method) {
		String staticKey = null;
		EJBCache ejbAnnotation = method.getAnnotation(EJBCache.class);
		RESTCache restAnnotation = method.getAnnotation(RESTCache.class);
		if (ejbAnnotation != null && !ejbAnnotation.staticKey().equals("")) {
			staticKey = ejbAnnotation.staticKey();
		}
		if (restAnnotation != null && !restAnnotation.staticKey().equals("")) {
			staticKey = restAnnotation.staticKey();
		}
		return staticKey;
	}
}
