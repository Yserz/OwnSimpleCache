package de.yser.ownsimplecache.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Michael Koppen
 * @param <K>
 * @param <V>
 */
public class CacheHashMap<K, V> {

	private final ConcurrentHashMap<K, ExpiringEntry<V>> cache;
	public final long DEFAULT_EXPIRING_TIME;

	public CacheHashMap() {
		this.DEFAULT_EXPIRING_TIME = 90000l;
		cache = new ConcurrentHashMap<>();
	}

	public CacheHashMap(long defaultExpiringTime) {
		this.DEFAULT_EXPIRING_TIME = defaultExpiringTime;
		cache = new ConcurrentHashMap<>();
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public int size() {
		return cache.size();
	}

	public V get(K key) {
		ExpiringEntry<V> entry = cache.get(key);
		if (entry != null) {
			return entry.getValue();
		} else {
			return null;
		}
	}

	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}

	public boolean containsValue(V value) {
		return cache.containsValue(new ExpiringEntry<>(value, DEFAULT_EXPIRING_TIME));
	}

	public boolean contains(V value) {
		return cache.contains(new ExpiringEntry<>(value, DEFAULT_EXPIRING_TIME));
	}

	public V put(K key, V value) {
		return this.put(key, value, DEFAULT_EXPIRING_TIME);
	}

	public V put(K key, V value, long expiringTime) {
		removeExpired();
		ExpiringEntry<V> previous = cache.put(key, new ExpiringEntry<>(value, expiringTime));
		if (previous != null) {
			return previous.getValue();
		}
		return null;
	}

	public V putIfAbsent(K key, V value) {
		return putIfAbsent(key, value, DEFAULT_EXPIRING_TIME);
	}

	public V putIfAbsent(K key, V value, long expiringTime) {
		ExpiringEntry<V> previous = cache.putIfAbsent(key, new ExpiringEntry<>(value, expiringTime));
		if (previous != null) {
			return previous.getValue();
		}
		return null;
	}

	public void putAll(CacheHashMap<K, V> m) {
		this.putAll(m, DEFAULT_EXPIRING_TIME);
	}

	// Lambda-way
//	public void putAll(CacheHashMap<K, V> m, long expiringTime) {
//		final HashMap<K, ExpiringEntry<V>> newMap = new HashMap<>();
//		m.entrySet().stream().forEach((entry) -> {
//			newMap.put(entry.getKey(), new ExpiringEntry<>(entry.getValue(), expiringTime));
//		});
//		cache.putAll(newMap);
//	}
	public void putAll(CacheHashMap<K, V> m, long expiringTime) {
		HashMap<K, ExpiringEntry<V>> newMap = new HashMap<>();
		for (Map.Entry<K, V> entry : m.entrySet()) {
			newMap.put(entry.getKey(), new ExpiringEntry<>(entry.getValue(), expiringTime));
		}
		cache.putAll(newMap);
	}

	public V remove(K key) {
		ExpiringEntry<V> previous = cache.remove(key);
		if (previous != null) {
			return previous.getValue();
		}
		return null;
	}

	public boolean remove(K key, V value) {
		return cache.remove(key, new ExpiringEntry<>(value, DEFAULT_EXPIRING_TIME));
	}

	/**
	 * replaces the old value by key and value with default expriring time
	 * reset.
	 *
	 * @param key
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public boolean replace(K key, V oldValue, V newValue) {
		return this.replace(key, oldValue, newValue, DEFAULT_EXPIRING_TIME);
	}

	public boolean replace(K key, V oldValue, V newValue, long expiringTime) {
		return cache.replace(key, new ExpiringEntry<>(oldValue, DEFAULT_EXPIRING_TIME), new ExpiringEntry<>(newValue, expiringTime));
	}

	/**
	 * replaces the old value by key with default expiring time reset.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public V replace(K key, V value) {
		return this.replace(key, value, DEFAULT_EXPIRING_TIME);
	}

	public V replace(K key, V value, long expiringTime) {
		return cache.replace(key, new ExpiringEntry<>(value, expiringTime)).getValue();
	}

	public void clear() {
		cache.clear();
	}

	public Set<K> keySet() {
		return cache.keySet();
	}

	// Lambda-way
//	public Collection<V> values() {
//		return cache.values().stream().
//			map((entry) -> entry.getValue()).
//			collect(Collectors.toCollection(ArrayList::new));
//	}
	public Collection<V> values() {
		Collection values = new ArrayList<>();
		for (ExpiringEntry<V> entry : cache.values()) {
			values.add(entry.getValue());
		}
		return values;
	}

	// Lambda-way
//	public Set<Map.Entry<K, V>> entrySet() {
//		return cache.entrySet().stream().
//			map((expEntry) -> new AbstractMap.SimpleEntry<>(expEntry.getKey(), expEntry.getValue().getValue())).
//			collect(Collectors.toCollection(HashSet::new));
//	}
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> entrySet = new HashSet<>();
		for (Map.Entry<K, ExpiringEntry<V>> expEntry : cache.entrySet()) {
			Map.Entry<K, V> entry = new AbstractMap.SimpleEntry<>(expEntry.getKey(), expEntry.getValue().getValue());
			entrySet.add(entry);
		}
		return entrySet;
	}

	public Enumeration<K> keys() {
		return cache.keys();
	}

	public Enumeration<V> elements() {
		Set<V> elements = new HashSet<>();
		Enumeration<ExpiringEntry<V>> oldElements = cache.elements();
		while (oldElements.hasMoreElements()) {
			elements.add(oldElements.nextElement().getValue());
		}
		return Collections.enumeration(elements);
	}

	// Lambda-way
//	protected void removeExpired() {
//		cache.entrySet().stream().parallel().forEach((entry) -> {
//			if (entry.getValue().isExpiried()) {
//				cache.remove(entry.getKey());
//			}
//		});
//	}
	protected void removeExpired() {
		for (Map.Entry<K, ExpiringEntry<V>> entry : cache.entrySet()) {
			K k = entry.getKey();
			ExpiringEntry<V> expiringEntry = entry.getValue();
			if (expiringEntry.isExpiried()) {
				cache.remove(k);
			}
		}
	}

}
