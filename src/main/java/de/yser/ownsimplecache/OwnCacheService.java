package de.yser.ownsimplecache;

import de.yser.ownsimplecache.cache.CacheHashMap;
import de.yser.ownsimplecache.util.clear.ClearCacheInterceptor;
import de.yser.ownsimplecache.util.clear.ClearCachesInterceptor;
import de.yser.ownsimplecache.util.hook.Hook;
import de.yser.ownsimplecache.util.hook.Registry;
import de.yser.ownsimplecache.util.hook.logging.OwnSimpleCacheLoggingHook;
import de.yser.ownsimplecache.util.jaxrs.EntityTagGenerator;
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
 * @author Michael Koppen
 */
@Singleton
public class OwnCacheService {

	private static final Logger LOG = Logger.getLogger(OwnCacheService.class.getName());
	public final long DEFAULT_EXPIRING_TIME = 90000l;
	private Map<String, CacheHashMap> caches;
	private final List<Hook> hooks = new ArrayList<>();

	static {
		Logger.getLogger(OwnCacheService.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(OwnSimpleCacheLoggingHook.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(OwnCacheServerService.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(OwnCacheListener.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(de.yser.ownsimplecache.util.jaxrs.KeyGenerator.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(de.yser.ownsimplecache.util.ejb.KeyGenerator.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(EntityTagGenerator.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(de.yser.ownsimplecache.util.jaxrs.CacheInterceptor.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(de.yser.ownsimplecache.util.ejb.CacheInterceptor.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(ClearCachesInterceptor.class.getName())
			.setLevel(Level.INFO);
		Logger.getLogger(ClearCacheInterceptor.class.getName())
			.setLevel(Level.INFO);

		try {
			OwnCacheService.registerHooks(Hook.class, OwnSimpleCacheLoggingHook.class);
		} catch (Exception ex) {
			Logger.getLogger(OwnCacheService.class.getName()).log(Level.SEVERE, "Exception on registering Hooks!", ex);
		}
	}

	public OwnCacheService() {
		caches = new ConcurrentHashMap<>();

		for (Hook hook : Registry.getClasses(Hook.class)) {
			LOG.log(Level.INFO, "Hook-Class: {0}", hook.getClass().getName());
			hooks.add(hook);
		}

	}

	@PostConstruct
	private void init() {

	}

	public static void registerHooks(Class<?> hookType, Class<?>... hooks) throws Exception {
		for (Class<?> hook : hooks) {
			Registry.registerClass(hookType, hook);
		}
	}

	public <T> void cache(String key, Class<? extends T> asType, T value) {
		cache(key, asType, value, null);
	}

	/**
	 * Caches a value in a cache of the given type. The value must be an instance of the given type. If the Cache doesnt
	 * exists a cache will be created and the value will be cached in the new cache.
	 * <p>
	 * @param <T>             Type of the value to cache.
	 * @param key             Key of the value.
	 * @param asType          The official type of the cache.
	 * @param value           Value to cache.
	 * @param genericTypeHint
	 */
	public synchronized <T> void cache(String key, Class<? extends T> asType, T value, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{key, asType}) || value == null) {
			throw new IllegalArgumentException("Can't cache with null-value, null-key, empty key, null-type or empty lists!");
		}
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.willCache(asType.getName(), key, value.toString());
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}
		}
		CacheHashMap foundCache = getOrCreateCache(asType, genericTypeHint);
//		LOG.log(Level.INFO, " -	Caching object of class {0} as {1} with key {2}.", new Object[]{value.getClass().getName(), asType.getName(), key});

		foundCache.put(key, value);
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.didCache(asType.getName(), key, value.toString());
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}

		}

	}

	public <T> T get(String key, Class<? extends T> type) {
		return this.get(key, type, null);
	}

	public synchronized <T> T get(String key, Class<? extends T> type, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{key, type})) {
			throw new IllegalArgumentException("Can't get value with null-class, null-key or empty key!");
		}
		for (Hook monitorHook : hooks) {

			try {
				monitorHook.willGet(type.getName(), key);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}
		}

		T result = null;
		CacheHashMap foundCache;
		if (genericTypeHint != null) {
			foundCache = caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			foundCache = caches.get(type.getName());
		}

		if (foundCache != null) {
//			LOG.log(Level.INFO, " -	Getting object of class {0} with key {1}.", new Object[]{type.getName(), key});
			if (foundCache.containsKey(key)) {
				result = type.cast(foundCache.get(key));
				for (Hook monitorHook : hooks) {
					try {
						monitorHook.didGet(type.getName(), key);
					} catch (Exception e) {
						LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
						LOG.log(Level.INFO, "HookException: ", e);
					}
				}
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
	 * <p>
	 * @param <T>             Type of the value.
	 * @param key             Key of the value.
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
//			LOG.log(Level.INFO, " -	Invalidating object of class {0}.", new Object[]{type.getName()});
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
	 * <p>
	 * @param clazzString     String of class type (e.g. java.lang.String)
	 * @param genericTypeHint
	 */
	public synchronized void invalidateCache(String clazzString, String genericTypeHint) {
		if (isAnyNullOrEmpty(new Object[]{clazzString})) {
			LOG.log(Level.WARNING, " -	Can't invalidate with null-class!");
			throw new IllegalArgumentException("Can't invalidate with null-class!");
		}
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.willInvalidateCache(clazzString, genericTypeHint);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}
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
			for (Hook monitorHook : hooks) {
				try {
					monitorHook.didInvalidateCache(clazzString, genericTypeHint);
				} catch (Exception e) {
					LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
					LOG.log(Level.INFO, "HookException: ", e);
				}

			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(OwnCacheService.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void invalidateAllCaches() {
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.willInvalidateAllCaches();
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}

		}
		caches = new ConcurrentHashMap<>();
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.didInvalidateAllCaches();
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}
		}
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
			LOG.log(Level.WARNING, "Can't create cache with null-values or expiringtime <= 0 !");
			throw new IllegalArgumentException("Can't create cache with null-values or expiringtime <= 0 !");
		}
		for (Hook monitorHook : hooks) {
			try {
				monitorHook.willCreateCache(type.getName(), genericTypeHint, expiringTime, unit);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
				LOG.log(Level.INFO, "HookException: ", e);
			}

		}

		if (genericTypeHint != null) {
			caches.put(type.getName() + "<" + genericTypeHint + ">", createTypedCache(expiringTime, unit));
			for (Hook monitorHook : hooks) {
				try {
					monitorHook.didCreateCache(type.getName(), genericTypeHint, expiringTime, unit);
				} catch (Exception e) {
					LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
					LOG.log(Level.INFO, "HookException: ", e);
				}

			}
			return caches.get(type.getName() + "<" + genericTypeHint + ">");
		} else {
			caches.put(type.getName(), createTypedCache(expiringTime, unit));
			for (Hook monitorHook : hooks) {
				try {
					monitorHook.didCreateCache(type.getName(), genericTypeHint, expiringTime, unit);
				} catch (Exception e) {
					LOG.log(Level.WARNING, "Exception while running hook of type \"" + monitorHook.getClass().getName() + "\". Raise the LogLevel to INFO to see the stacktrace.", e.getMessage());
					LOG.log(Level.INFO, "HookException: ", e);
				}

			}
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
