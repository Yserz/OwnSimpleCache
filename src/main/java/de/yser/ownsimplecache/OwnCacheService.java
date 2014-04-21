package de.yser.ownsimplecache;

import de.yser.ownsimplecache.cache.CacheHashMap;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.core.Response;

/**
 *
 * @author MacYser
 */
@Singleton
public class OwnCacheService {

	private static final Logger LOG = Logger.getLogger(OwnCacheService.class.getName());
	public final long DEFAULT_EXPIRING_TIME = 90000l;
	private Map<String, CacheHashMap> caches;

	public OwnCacheService() {
		caches = new ConcurrentHashMap<>();
	}

	@PostConstruct
	private void init() {
	}

	public <T> void cache(String key, Class<? extends T> asType, T value) {
		cache(key, asType, value, null);
	}

	/**
	 * Caches a value in a cache of the given type. The value must be an
	 * instance of the given type. If the Cache doesnt exists a cache will be
	 * created and the value will be cached in the new cache.
	 *
	 * @param <T> Type of the value to cache.
	 * @param key Key of the value.
	 * @param asType The official type of the cache.
	 * @param value Value to cache.
	 * @param genericTypeHint
	 */
	public synchronized <T> void cache(String key, Class<? extends T> asType, T value, String genericTypeHint) {
		System.out.println("CACHE is called!");
		if (isAnyNullOrEmpty(new Object[]{key, asType}) || value == null) {
			throw new IllegalArgumentException("Can't cache with null-value, null-key, empty key, null-type or empty lists!");
		}

		CacheHashMap foundCache = getOrCreateCache(asType, genericTypeHint);
		LOG.log(Level.INFO, " -	Caching object of class {0} as {1} with key {2}.", new Object[]{value.getClass().getName(), asType.getName(), key});
		foundCache.put(key, value);

	}

	public <T> T get(String key, Class<? extends T> type) {
		return this.get(key, type, null);
	}

	public synchronized <T> T get(String key, Class<? extends T> type, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{key, type})) {
			throw new IllegalArgumentException("Can't get value with null-class, null-key or empty key!");
		}

		T result = null;
		CacheHashMap foundCache;
		if (genericTypeHint != null) {
			foundCache = caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			foundCache = caches.get(type.getName());
		}

		if (foundCache != null) {
			LOG.log(Level.INFO, " -	Getting object of class {0} with key {1}.", new Object[]{type.getName(), key});
			if (foundCache.containsKey(key)) {
				result = type.cast(foundCache.get(key));
			}
		} else {
			// TODO maybe create cache with default-values
			LOG.log(Level.WARNING, " -	No cache for objects of class {0} instantiated.", new Object[]{type.getName()});
		}
		return result;
	}

	public <T> void invalidateValue(String key, Class<? extends T> type) {
		invalidateValue(key, type, null);
	}

	/**
	 * Invalidates a cached value with the given key and the type of the value.
	 *
	 * @param <T> Type of the value.
	 * @param key Key of the value.
	 * @param type
	 * @param genericTypeHint
	 */
	public synchronized <T> void invalidateValue(String key, Class<? extends T> type, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{key, type})) {
			LOG.log(Level.WARNING, " -	Can't cache with null-value, null-key, empty key or empty lists!");
			throw new IllegalArgumentException("Can't cache with null-value, null-key, empty key or empty lists!");
		}

		CacheHashMap foundCache;
		if (genericTypeHint != null) {
			foundCache = caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			foundCache = caches.get(type.getName());
		}

		if (foundCache != null) {
			LOG.log(Level.INFO, " -	Invalidating object of class {0}.", new Object[]{type.getName()});
			foundCache.remove(key);
		} else {
			LOG.log(Level.WARNING, " -	No cache for objects of class {0} instantiated.", new Object[]{type.getName()});
		}
	}

	public void invalidateCache(String clazzString) {
		invalidateCache(clazzString, null);
	}

	/**
	 * Invalidates a cache with the given type of the values.
	 *
	 * @param clazzString String of class type (e.g. java.lang.String)
	 * @param genericTypeHint
	 */
	public synchronized void invalidateCache(String clazzString, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{clazzString})) {
			LOG.log(Level.WARNING, " -	Can't invalidate with null-class!");
			throw new IllegalArgumentException("Can't invalidate with null-class!");
		}

		ClassLoader cl = this.getClass().getClassLoader();
		Class<?> clazz;

		try {
			clazz = cl.loadClass(clazzString);
			List<Class<?>> interfaces = new ArrayList<>(Arrays.asList(clazz.getInterfaces()));

			if (clazz.isInterface()) {
				LOG.log(Level.FINEST, " -\t\t\tAdding {0} to interface list.", clazz.getName());
				interfaces.add(clazz);
			}

			List<Class<?>> superClasses = new ArrayList<>();
			Class cla = clazz;
			while (cla != null) {
				superClasses.add(cla);
				cla = cla.getSuperclass();
			}

			if (interfaces.contains(List.class)) {
				if (true) {
					if (caches.containsKey(List.class.getName() + "<" + genericTypeHint + ">")) {
						LOG.log(Level.INFO, " -	Invalidating cache of class List.");
						caches.remove(List.class.getName() + "<" + genericTypeHint + ">");
					} else {
						LOG.log(Level.WARNING, " -\tNo cache for class List<{0}> instantiated.", genericTypeHint);
					}
				} else {
					if (caches.containsKey(List.class.getName())) {
						LOG.log(Level.INFO, " -	Invalidating cache of class List.");
						caches.remove(List.class.getName());
					} else {
						LOG.log(Level.WARNING, " -	No cache for class List instantiated.");
					}
				}

			} else if (superClasses.contains(Response.class)) {
				if (caches.containsKey(Response.class.getName())) {
					LOG.log(Level.INFO, " -	Invalidating cache of class Response.");
					caches.remove(Response.class.getName());
				} else {
					LOG.log(Level.WARNING, " -	No cache for class Response instantiated.");
				}
			} else {
				if (caches.containsKey(clazz.getName())) {
					LOG.log(Level.INFO, " -	Invalidating cache of class {0}.", new Object[]{clazz.getName()});
					caches.remove(clazz.getName());
				} else {
					LOG.log(Level.WARNING, " -	No cache for class {0} instantiated.", new Object[]{clazz.getName()});
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(OwnCacheService.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void invalidateAllCaches() {
		caches = new ConcurrentHashMap<>();
	}

	@SuppressWarnings({"unchecked"})
	public void showCaches() {
		for (Entry<String, CacheHashMap> cacheEntry : caches.entrySet()) {
			LOG.log(Level.INFO, "-------------------------------------------------------\n"
				+ " +	Entries of Cache for {0} classes:", cacheEntry.getKey());
			CacheHashMap<String, Class<?>> aktCache = (CacheHashMap<String, Class<?>>) cacheEntry.getValue();
			for (Entry<String, Class<?>> kvEntry : aktCache.entrySet()) {
				LOG.log(Level.INFO, " +		Key: {0} --> ", kvEntry.getKey());
				LOG.log(Level.INFO, " -		{0}", kvEntry.getValue());
			}
		}
	}

	private <T> CacheHashMap getOrCreateCache(Class<T> type, String genericTypeHint) {
		CacheHashMap foundCache;
		if (genericTypeHint != null) {
			foundCache = caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			foundCache = caches.get(type.getName());
		}

		if (foundCache == null) {
			foundCache = createCache(type, genericTypeHint, DEFAULT_EXPIRING_TIME, TimeUnit.MILLISECONDS);
		}
		return foundCache;
	}

	public <T> CacheHashMap createCache(Class<T> type, long expiringTime, TimeUnit unit) {
		return createCache(type, null, expiringTime, unit);
	}

	public synchronized <T> CacheHashMap createCache(Class<T> type, String genericTypeHint, long expiringTime, TimeUnit unit) {
		if (isAnyNullOrEmpty(new Object[]{type, unit}) || expiringTime <= 0) {
			throw new IllegalArgumentException("Can't create cache with null-values or expiringtime <= 0 !");
		}

		if (genericTypeHint != null) {
			caches.put(type.getName() + "<" + genericTypeHint + ">", createTypedCache(expiringTime, unit));
			return caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			caches.put(type.getName(), createTypedCache(expiringTime, unit));
			return caches.get(type.getName());
		}

	}

	private CacheHashMap createTypedCache(long expiringTime, TimeUnit unit) {
		return new CacheHashMap<>(TimeUnit.MILLISECONDS.convert(expiringTime, unit));
	}

	private boolean isAnyNullOrEmpty(Object[] objectArray) {
		for (Object object : objectArray) {
			if (object == null) {
				return true;
			} else {
				if (object instanceof List) {
					if (((List) object).isEmpty()) {
						return true;
					}
				} else if (object instanceof String) {
					if (((String) object).isEmpty()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
