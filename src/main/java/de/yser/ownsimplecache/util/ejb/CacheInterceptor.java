package de.yser.ownsimplecache.util.ejb;

import de.yser.ownsimplecache.OwnCacheService;
import de.yser.ownsimplecache.util.CacheAnnotationReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@EJBCache
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 200)
public class CacheInterceptor {

	private static final Logger LOG = Logger.getLogger(CacheInterceptor.class.getName());

	@EJB
	private OwnCacheService cacheService;
	private KeyGenerator keyGen;
	private CacheAnnotationReader annoReader;

	public CacheInterceptor() {
		this.keyGen = new KeyGenerator();
		this.annoReader = new CacheAnnotationReader();
	}

	@AroundInvoke
	public Object cache(InvocationContext context) throws Exception {
		LOG.log(Level.INFO, "---------------------------------------------------------");
		LOG.log(Level.INFO, "CACHE: +	Accessing Cache");
		Method method = context.getMethod();
		Object result = null;

		// get information of annotation
		String staticKey = annoReader.getStaticKey(method);
		String genericTypeHint = annoReader.getGenericTypeHint(method);

		//TODO do something with genericTypeHint
		String key;
		if (staticKey == null) {
			key = keyGen.generateKey(method, context.getParameters());
		} else {
			key = staticKey;
		}

		result = getCachedValue(key, method.getReturnType());

		if (result == null) {
			LOG.log(Level.INFO, "CACHE: -		Theres no existing Value with this Key in Cache.");
			LOG.log(Level.INFO, "CACHE: -		Proceeding with source method.");
			//Do caching
			//executing sourceMethod body
			result = context.proceed();

			doCache(result, key);
		}

		LOG.log(Level.INFO, "CACHE: +		Returning Cached Result");
		return result;
	}

	private void doCache(Object result, String generatedKey) {
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -		Proceeding with caching of the source methods result.");

		List<Class<?>> interfaces = Arrays.asList(result.getClass().getInterfaces());
		if (interfaces.contains(List.class)) {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -			Caching value of class {0} in cache of type List with key {1}", new Object[]{result.getClass().getName(), generatedKey});
			cacheService.cache(generatedKey, List.class, result);
		} else {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -			Caching value of class {0} in cache of type {0} with key {1}", new Object[]{result.getClass().getName(), generatedKey});
			cacheService.cache(generatedKey, result.getClass(), result);
		}
	}

	private Object getCachedValue(String generatedKey, Class<?> returnType) {
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -		Trying to get Value out of Cache.");
		Object result = null;

		List<Class<?>> interfaces = new ArrayList<>(Arrays.asList(returnType.getInterfaces()));
		if (returnType.isInterface()) {
			System.out.println("Adding " + returnType.getName() + " to interface list.");
			interfaces.add(returnType);
		}

		if (interfaces.contains(List.class)) {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -			Looking for a List.");
			result = cacheService.get(generatedKey, List.class);
		} else {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -			Looking for an Object.");
			result = cacheService.get(generatedKey, returnType.getClass());
		}
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -			Result: {0}", result);
		return result;
	}

}
