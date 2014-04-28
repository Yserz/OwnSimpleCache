package de.yser.ownsimplecache;

import de.yser.ownsimplecache.cache.CacheHashMap;
import java.util.concurrent.TimeUnit;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Michael Koppen
 */
public class OwnCacheServiceTest {

	public OwnCacheServiceTest() {
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
	 * Test of cache method, of class OwnCacheService.
	 */
	@Test
	public void testCache() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("cache");
		OwnCacheService instance = new OwnCacheService();
		String key = null;
		Object clazz = null;
		try {
			key = null;
			clazz = null;
			instance.cache(key, Object.class, clazz);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		try {
			key = null;
			clazz = new Object();
			instance.cache(key, Object.class, clazz);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		try {
			key = "";
			clazz = new Object();
			instance.cache(key, Object.class, clazz);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		try {
			key = "";
			clazz = null;
			instance.cache(key, Object.class, clazz);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		try {
			key = "1";
			clazz = null;
			instance.cache(key, Object.class, clazz);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		key = "1";
		clazz = new Object();
		instance.cache(key, Object.class, clazz);

	}

	/**
	 * Test of invalidate method, of class OwnCacheService.
	 */
	@Test
	public void testInvalidate() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("invalidate");
		String key = null;
		Object clazz = null;
		OwnCacheService instance = new OwnCacheService();

		try {
			key = null;
			instance.invalidateValue(key, Object.class);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		try {
			key = "";
			instance.invalidateValue(key, Object.class);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		key = "1";
		clazz = new Object();

		instance.invalidateValue(key, Object.class);

		instance.cache(key, Object.class, clazz);
		instance.invalidateValue(key, Object.class);
	}

	/**
	 * Test of get method, of class OwnCacheService.
	 */
	@Test
	public void testGet() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("get");
		String key = "1";
		Object clazz = new Object();
		OwnCacheService instance = new OwnCacheService();
		Object expResult = null;
		Object result = instance.get(key, clazz.getClass());
		assertEquals(expResult, result);

		try {
			key = "";
			instance.invalidateValue(key, Object.class);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}
		try {
			key = null;
			instance.invalidateValue(key, Object.class);
			fail("Should have raised an exception!");
		} catch (IllegalArgumentException e) {
		}

		key = "1";
		clazz = new Object();
		expResult = clazz;
		instance.cache(key, Object.class, clazz);
		result = instance.get(key, Object.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of showCaches method, of class OwnCacheService.
	 */
	@Test
	public void testShowCaches() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("showCaches");
		OwnCacheService instance = new OwnCacheService();
		instance.cache("1", Object.class, new Object());
		instance.cache("2", Object.class, new Object());
		instance.showCaches();
	}

	/**
	 * Test of createCache method, of class OwnCacheService.
	 */
	@Test
	public void testCreateCache() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("createCache");
		long expiringTime = 90000l;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		OwnCacheService instance = new OwnCacheService();
		CacheHashMap cache = instance.createCache(Object.class, expiringTime, unit);
		assertTrue(cache.isEmpty());
	}

	/**
	 * Test of clearAllCaches method, of class OwnCacheService.
	 */
	@Test
	public void testClearAllCaches() throws Exception {
		System.out.println("\n\n#############################");
		System.out.println("clearAllCaches");
		OwnCacheService instance = new OwnCacheService();
		instance.invalidateAllCaches();
		assertEquals(null, instance.get("1", new Object().getClass()));
	}

}
