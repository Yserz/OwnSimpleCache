package de.yser.ownsimplecache.util.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.PathParam;

/**
 *
 * @author Michael Koppen
 */
public class KeyGenerator {

	private static final Logger LOG = Logger.getLogger(KeyGenerator.class.getName());

	public KeyGenerator() {
	}

	String generateKey(Method method, Object[] params) {
		String generatedKey = method.getName();
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: +\t\tGenerating cacheKey");
		LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: +\t\t\t\t\tParameter Annotations:");
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();

		int i = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			Class parameterType = parameterTypes[i];
			for (Annotation annotation : annotations) {
				if (annotation instanceof PathParam) {
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\t\t\tpType: {0}", parameterType.getName());
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\t\t\tpValue: {0}", params[i]);
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\t\t\taType: {0}", annotation.annotationType().getName());
					LOG.log(Level.FINEST, "CACHE-INTERCEPTOR: -\t\t\t\t\t\taValue: {0}", ((PathParam) annotation).value());
					generatedKey = generatedKey + "-" + ((PathParam) annotation).value() + "-" + params[i];
				}
			}
			i++;
		}
		LOG.log(Level.INFO, "CACHE-INTERCEPTOR: -\t\t\tGenerated key is {0}", new Object[]{generatedKey});
		return generatedKey;
	}
}
