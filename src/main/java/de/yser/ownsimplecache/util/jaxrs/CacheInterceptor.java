package de.yser.ownsimplecache.util.jaxrs;

import de.yser.ownsimplecache.OwnCacheService;
import de.yser.ownsimplecache.util.CacheAnnotationReader;
import java.lang.annotation.Annotation;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *
 * @author MacYser
 */
@RESTCache
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 200)
public class CacheInterceptor {

	@EJB
	private OwnCacheService cacheService;
	private EntityTagGenerator etGen;
	private KeyGenerator keyGen;
	private CacheAnnotationReader annoReader;
	private static final Logger LOG = Logger.getLogger(CacheInterceptor.class.getName());

	public CacheInterceptor() {
		LOG.setLevel(Level.FINEST);
		this.etGen = new EntityTagGenerator();
		this.keyGen = new KeyGenerator();
		this.annoReader = new CacheAnnotationReader();

	}

	@AroundInvoke
	public Object cache(InvocationContext context) throws Exception {
		LOG.log(Level.INFO, "---------------------------------------------------------");
		cacheService.showCaches();
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\tAccessing Cache");
		Method method = context.getMethod();
		Object result = null;

		//just cache if its a RESTCache- or EJBCache-Method
		if (method.isAnnotationPresent(RESTCache.class)) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\tMethod: {0} is a @RESTCache or @EJBCache method.", method.getName());

			// get information of annotation
			String staticKey = annoReader.getStaticKey(method);
			String genericTypeHint = annoReader.getGenericTypeHint(method);

			String key;
			if (staticKey == null) {
				key = keyGen.generateKey(method, context.getParameters());
			} else {
				key = staticKey;
			}

			Request req = this.getRequest(method, context.getParameters());
			result = getCachedValue(key, method.getReturnType(), genericTypeHint, req);

			if (result == null) {
				LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tTheres no existing Value with this Key in Cache. Proceeding with source method.");
				//Do caching
				//executing sourceMethod body
				result = context.proceed();

				doCache(result, key, genericTypeHint);
			}
		} else {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -\t\tMethod {0} is not a @RESTCache method, Continue without any further changes.", method.getName());
			result = context.proceed();
		}
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tReturning Cached Result");
		cacheService.showCaches();
		return result;
	}

	private void doCache(Object result, String generatedKey, String genericTypeHint) {
		LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\tProceeding with caching of the source methods result.");

		List<Class<?>> interfaces = new ArrayList<>(Arrays.asList(result.getClass().getInterfaces()));
		if (result.getClass().isInterface()) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tAdding {0} to interface list.", result.getClass().getName());
			interfaces.add(result.getClass());
		}
		for (Class<?> class1 : interfaces) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tInterfaces: {0}", class1);
		}

		List<Class<?>> superClasses = new ArrayList<>();
		Class clazz = result.getClass();
		while (clazz != null) {
			superClasses.add(clazz);
			clazz = clazz.getSuperclass();
		}
		for (Class<?> class1 : superClasses) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tSuperclass: {0}", class1);
		}
		if (superClasses.contains(Response.class)) {
			if (((Response) result).hasEntity()) {
				result = etGen.generateTaggedResponse((Response) result);
				LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tCaching value of class {0} in cache of type Response with key {1}", new Object[]{result.getClass().getName(), generatedKey});
				cacheService.cache(generatedKey, Response.class, result);
			} else {
				LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tSkip caching because Response has no entity!");
			}
		} else if (interfaces.contains(List.class)) {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tCaching value of class {0} in cache of type List with key {1}", new Object[]{result.getClass().getName(), generatedKey});
			cacheService.cache(generatedKey, List.class, result, genericTypeHint);
		} else {
			LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tCaching value of class {0} in cache of type {0} with key {1}", new Object[]{result.getClass().getName(), generatedKey});
			cacheService.cache(generatedKey, result.getClass(), result);
		}

	}

	private Object getCachedValue(String generatedKey, Class<?> returnType, String genericTypeHint, Request req) {
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tTrying to get Value out of Cache.");
		Object result = null;

		List<Class<?>> interfaces = new ArrayList<>(Arrays.asList(returnType.getInterfaces()));
		if (returnType.isInterface()) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\tAdding {0} to interface list.", returnType.getName());
			interfaces.add(returnType);
		}
		for (Class<?> class1 : interfaces) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\tInterfaces: {0}", class1);
		}

		List<Class<?>> superClasses = new ArrayList<>();
		Class clazz = returnType;
		while (clazz != null) {
			superClasses.add(clazz);
			clazz = clazz.getSuperclass();
		}
		for (Class<?> class1 : superClasses) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\tSuperclass: {0}", class1);
		}

		if (interfaces.contains(List.class)) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tLooking for a List.");
			result = cacheService.get(generatedKey, List.class, genericTypeHint);
		} else if (superClasses.contains(Response.class)) {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tLooking for a Response.");
			result = cacheService.get(generatedKey, Response.class);
			if (result != null) {
				result = etGen.generateTaggedResponseOrNotModified((Response) result, req);
			}
		} else {
			LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\tLooking for an Object.");
			result = cacheService.get(generatedKey, returnType);
		}
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -\t\t\tResult: {0}", result);
		return result;
	}

	private Request getRequest(Method method, Object[] params) {
		Request req = null;
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tGetting Request");
		LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: +\t\t\tParameter Annotations:");
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();

		int i = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			Class parameterType = parameterTypes[i];
			for (Annotation annotation : annotations) {
				if (annotation instanceof Context) {
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\tpType: {0}", parameterType.getName());
					// Dont print Requests out of request-scope
//					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -				pValue: {0}", params[i]);
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\taType: {0}", annotation.annotationType().getName());
					if (parameterType.getName().equalsIgnoreCase("javax.ws.rs.core.Request")) {
						req = (Request) params[i];
					}
				}
			}
			i++;
		}
		if (req == null) {
			LOG.log(Level.WARNING, "CACHE-INTERCEPTOR: -\tCould not get Request from Method {0}\n"
				+ "be sure \"@Context Request req\" is in the parameter list of this method!", new Object[]{method.getName()});
		}
		return req;
	}

}
