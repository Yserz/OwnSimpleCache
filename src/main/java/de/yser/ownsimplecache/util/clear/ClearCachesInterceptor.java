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
 * @author MacYser
 */
@ClearCaches(
	@ClearCache
)
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 200)
public class ClearCachesInterceptor {

	@EJB
	private OwnCacheServerService cacheServer;
	private final CacheAnnotationReader annoReader;
	private static final Logger LOG = Logger.getLogger(ClearCachesInterceptor.class.getName());

	public ClearCachesInterceptor() {
		this.annoReader = new CacheAnnotationReader();
	}

	@AroundInvoke
	public Object invalidate(InvocationContext context) throws Exception {
		Method method = context.getMethod();

		if (method.isAnnotationPresent(ClearCaches.class)) {
			LOG.log(Level.INFO, "---------------------------------------------------------");
			LOG.log(Level.INFO, "CLEAR_CACHES_INTERCEPTOR: +	Invalidating Caches");

			ClearCaches clearCachesAnno = method.getAnnotation(ClearCaches.class);
			ClearCache[] clearCacheAnnos = clearCachesAnno.value();

			for (ClearCache clearCache : clearCacheAnnos) {
				//TODO use annotationReader instead
				String cacheType = clearCache.cacheType().equals("") ? null : clearCache.cacheType();
				String genericTypeHint = clearCache.genericTypeHint().equals("") ? null : clearCache.genericTypeHint();
				boolean invalidateAllCaches = clearCache.invalidateAllCaches();

				if (invalidateAllCaches) {
					cacheServer.invalidateAllCaches();
					break;
				} else {
					if (cacheType == null) {
						cacheServer.invalidateCache(method.getReturnType().toString(), genericTypeHint);
					} else {
						cacheServer.invalidateCache(cacheType, genericTypeHint);
					}
				}
			}

		}

		//executing sourceMethod body
		Object result = context.proceed();
		return result;
	}
}
