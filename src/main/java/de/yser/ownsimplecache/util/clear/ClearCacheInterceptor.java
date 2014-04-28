package de.yser.ownsimplecache.util.clear;

import de.yser.ownsimplecache.OwnCacheServerService;
import de.yser.ownsimplecache.util.CacheAnnotationReader;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Michael Koppen
 */
@ClearCache
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 200)
public class ClearCacheInterceptor {

	@EJB
	private OwnCacheServerService cacheServer;
	private final CacheAnnotationReader annoReader;
	private static final Logger LOG = Logger.getLogger(ClearCacheInterceptor.class.getName());

	public ClearCacheInterceptor() {
		this.annoReader = new CacheAnnotationReader();
	}

	@AroundInvoke
	public Object invalidate(InvocationContext context) throws Exception {
		Method method = context.getMethod();

		if (method.isAnnotationPresent(ClearCache.class)) {
			LOG.log(Level.INFO, "---------------------------------------------------------");
			LOG.log(Level.INFO, "CLEAR_CACHE_INTERCEPTOR: +	Invalidating Caches");

			String cacheType = annoReader.getCacheType(method);
			String genericTypeHint = annoReader.getGenericTypeHint(method);
			boolean invalidateAllCaches = annoReader.getInvalidateAllCaches(method);

			if (invalidateAllCaches) {
				cacheServer.invalidateAllCaches();
			} else {
				if (cacheType == null) {
					cacheServer.invalidateCache(method.getReturnType().toString(), genericTypeHint);
				} else {
					cacheServer.invalidateCache(cacheType, genericTypeHint);
				}
			}
		}

		//executing sourceMethod body
		Object result = context.proceed();
		return result;
	}
}
