package de.yser.ownsimplecache.cache;

import java.util.*;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Michael Koppen
 */
public class CacheHashMapTest {

	private static final int STUB_CACHE_SIZE = 10;

	public CacheHashMapTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of isEmpty method, of class CacheHashMap.
	 *
	 * @throws java.lang.InterruptedException
	 */
	@Test
	public void testRemoveExpired() throws InterruptedException {
		System.out.println("removeExpired");
		String key = "2";
		String value = "2";
		CacheHashMap<String, String> instance = new CacheHashMap<>(1L);
		instance.put(key, value);
		assertEquals(false, instance.isEmpty());
		Thread.sleep(2L);
		instance.removeExpired();
		assertEquals(true, instance.isEmpty());

	}

	/**
	 * Test of isEmpty method, of class CacheHashMap.
	 */
	@Test
	public void testIsEmpty() {
		System.out.println("isEmpty");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(false, instance.isEmpty());

		instance = new CacheHashMap<>();
		assertEquals(true, instance.isEmpty());
	}

	/**
	 * Test of size method, of class CacheHashMap.
	 */
	@Test
	public void testSize() {
		System.out.println("size");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(STUB_CACHE_SIZE, instance.size());

		instance = new CacheHashMap<>();
		assertEquals(0, instance.size());
	}

	/**
	 * Test of get method, of class CacheHashMap.
	 */
	@Test
	public void testGet() {
		System.out.println("get");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals("2", instance.get("2"));
	}

	/**
	 * Test of containsKey method, of class CacheHashMap.
	 */
	@Test
	public void testContainsKey() {
		System.out.println("containsKey");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(true, instance.containsKey("2"));

		instance = new CacheHashMap<>();
		assertEquals(false, instance.containsKey("2"));
	}

	/**
	 * Test of containsValue method, of class CacheHashMap.
	 */
	@Test
	public void testContainsValue() {
		System.out.println("containsValue");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(true, instance.containsValue("2"));

		instance = new CacheHashMap<>();
		assertEquals(false, instance.containsValue("2"));
	}

	/**
	 * Test of contains method, of class CacheHashMap.
	 */
	@Test
	public void testContains() {
		System.out.println("contains");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(true, instance.contains("2"));

		instance = new CacheHashMap<>();
		assertEquals(false, instance.contains("2"));
	}

	/**
	 * Test of put method, of class CacheHashMap.
	 */
	@Test
	public void testPut_GenericType_GenericType() {
		System.out.println("put");
		CacheHashMap<String, String> instance = new CacheHashMap<>();

		String previous = instance.put("2", "2");
		assertEquals("2", instance.get("2"));
		assertEquals(null, previous);
	}

	/**
	 * Test of put method, of class CacheHashMap.
	 */
	@Test
	public void testPut_3args() {
		System.out.println("put");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		String previous = instance.put("2", "2", 1L);
		assertEquals("2", instance.get("2"));
		assertEquals(null, previous);
	}

	/**
	 * Test of putIfAbsent method, of class CacheHashMap.
	 */
	@Test
	public void testPutIfAbsent_GenericType_GenericType() {
		System.out.println("putIfAbsent");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		String previous = instance.putIfAbsent("2", "2");
		assertEquals("2", instance.get("2"));
		assertEquals(null, previous);

		previous = instance.putIfAbsent("2", "1");
		assertEquals("2", instance.get("2"));
		assertEquals("2", previous);
	}

	/**
	 * Test of putIfAbsent method, of class CacheHashMap.
	 */
	@Test
	public void testPutIfAbsent_3args() {
		System.out.println("putIfAbsent");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		String previous = instance.putIfAbsent("2", "2", 1L);
		assertEquals("2", instance.get("2"));
		assertEquals(null, previous);

		previous = instance.putIfAbsent("2", "1", 1L);
		assertEquals("2", instance.get("2"));
		assertEquals("2", previous);
	}

	/**
	 * Test of putAll method, of class CacheHashMap.
	 */
	@Test
	public void testPutAll_CacheHashMap() {
		System.out.println("putAll");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		CacheHashMap<String, String> putAll = new CacheHashMap<>();
		for (int i = 0; i < STUB_CACHE_SIZE; i++) {
			putAll.put(Integer.toString(i), Integer.toString(i));
		}
		instance.putAll(putAll);
		assertEquals(putAll.size(), instance.size());
	}

	/**
	 * Test of putAll method, of class CacheHashMap.
	 */
	@Test
	public void testPutAll_CacheHashMap_long() {
		System.out.println("putAll");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		CacheHashMap<String, String> putAll = new CacheHashMap<>();
		for (int i = 0; i < STUB_CACHE_SIZE; i++) {
			putAll.put(Integer.toString(i), Integer.toString(i));
		}
		instance.putAll(putAll, 1L);
		assertEquals(putAll.size(), instance.size());
	}

	/**
	 * Test of remove method, of class CacheHashMap.
	 */
	@Test
	public void testRemove_GenericType() {
		System.out.println("remove");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		String result = instance.remove("2");
		assertEquals(null, instance.get("2"));
		assertEquals(STUB_CACHE_SIZE - 1, instance.size());
		assertEquals("2", result);
	}

	/**
	 * Test of remove method, of class CacheHashMap.
	 */
	@Test
	public void testRemove_GenericType_GenericType() {
		System.out.println("remove");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		boolean result = instance.remove("2", "1");
		assertEquals(false, result);
		assertEquals("2", instance.get("2"));
		assertEquals(STUB_CACHE_SIZE, instance.size());

		result = instance.remove("2", "2");
		assertEquals(true, result);
		assertEquals(null, instance.get("2"));
		assertEquals(STUB_CACHE_SIZE - 1, instance.size());
	}

	/**
	 * Test of replace method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testReplace_3args_1() {
		System.out.println("replace");
		String key = null;
		String oldValue = null;
		String newValue = null;
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		boolean expResult = false;
		boolean result = instance.replace(key, oldValue, newValue);
		assertEquals(expResult, result);
	}

	/**
	 * Test of replace method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testReplace_4args() {
		System.out.println("replace");
		String key = null;
		String oldValue = null;
		String newValue = null;
		long expiringTime = 0L;
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		boolean expResult = false;
		boolean result = instance.replace(key, oldValue, newValue, expiringTime);
		assertEquals(expResult, result);
	}

	/**
	 * Test of replace method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testReplace_GenericType_GenericType() {
		System.out.println("replace");
		String key = null;
		String value = null;
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		String expResult = null;
		String result = instance.replace(key, value);
		assertEquals(expResult, result);
	}

	/**
	 * Test of replace method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testReplace_3args_2() {
		System.out.println("replace");
		String key = null;
		String value = null;
		long expiringTime = 0L;
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		String expResult = null;
		Object result = instance.replace(key, value, expiringTime);
		assertEquals(expResult, result);
	}

	/**
	 * Test of clear method, of class CacheHashMap.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		assertEquals(STUB_CACHE_SIZE, instance.size());
		instance.clear();
		assertEquals(0, instance.size());
	}

	/**
	 * Test of keySet method, of class CacheHashMap.
	 */
	@Test
	public void testKeySet() {
		System.out.println("keySet");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		Set<String> result = instance.keySet();
		assertEquals(STUB_CACHE_SIZE, result.size());

		instance.clear();
		result = instance.keySet();
		assertEquals(0, result.size());
	}

	/**
	 * Test of values method, of class CacheHashMap.
	 */
	@Test
	public void testValues() {
		System.out.println("values");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		fillCache(instance);
		Set<String> result = instance.keySet();
		assertEquals(STUB_CACHE_SIZE, result.size());

		instance.clear();
		result = instance.keySet();
		assertEquals(new HashSet<>(), result);
	}

	/**
	 * Test of entrySet method, of class CacheHashMap.
	 */
	@Test
	public void testEntrySet() {
		System.out.println("entrySet");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		assertEquals(new HashSet<>(), instance.entrySet());

		fillCache(instance);
		assertEquals(STUB_CACHE_SIZE, instance.entrySet().size());
	}

	/**
	 * Test of keys method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testKeys() {
		System.out.println("keys");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		Enumeration expResult = null;
		Enumeration result = instance.keys();
		assertEquals(expResult, result);
	}

	/**
	 * Test of elements method, of class CacheHashMap.
	 */
	@Ignore
	@Test
	public void testElements() {
		System.out.println("elements");
		CacheHashMap<String, String> instance = new CacheHashMap<>();
		Enumeration expResult = null;
		Enumeration result = instance.elements();
		assertEquals(expResult, result);
	}

	private void fillCache(CacheHashMap<String, String> instance) {
		for (int i = 0; i < STUB_CACHE_SIZE; i++) {
			instance.put(Integer.toString(i), Integer.toString(i));
		}
	}

}
