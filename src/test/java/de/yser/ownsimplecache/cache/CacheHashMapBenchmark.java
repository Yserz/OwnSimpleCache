/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.yser.ownsimplecache.cache;

import org.junit.*;
import org.openjdk.jmh.annotations.*;

/**
 *
 * @author MacYser
 */
@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class CacheHashMapBenchmark {

	static CacheHashMap<String, String> cacheEmpty = new CacheHashMap<>(90000l);
	static CacheHashMap<String, String> cache100 = new CacheHashMap<>(90000l);

	private static void fillCache100() {
		for (int i = 0; i < 100; i++) {
			cache100.put(Integer.toString(i), Integer.toString(i));
		}
	}

	static CacheHashMap<String, String> cache1000 = new CacheHashMap<>(90000l);

	private static void fillCache1000() {
		for (int i = 0; i < 1_000; i++) {
			cache1000.put(Integer.toString(i), Integer.toString(i));
		}
	}

	public CacheHashMapBenchmark() {
		fillCache100();
		fillCache1000();
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

	// #### ENTRYSET ####
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench100EntrySet() {
		cache100.entrySet();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench100EntrySetOLD() {
//		cache100.entrySetOLD();
//	}
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench1000EntrySet() {
		cache1000.entrySet();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench1000EntrySetOLD() {
//		cache1000.entrySetOLD();
//	}
	// #### ENTRYSET ####
	// #### VALUES ####
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench100Values() {
		cache100.values();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench100ValuesOLD() {
//		cache100.valuesOLD();
//	}
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench1000Values() {
		cache1000.values();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench1000ValuesOLD() {
//		cache1000.valuesOLD();
//	}
	// #### VALUES ####
	// #### PUT_ALL ####
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench100PutAll() {
		cache100.putAll(cache100);
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench100PutAllOLD() {
//		cache100.putAllOLD(cache100);
//	}
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench1000PutAll() {
		cache1000.putAll(cache1000);
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench1000PutAllOLD() {
//		cache1000.putAllOLD(cache1000);
//	}
	// #### PUT_ALL ####
	// #### REMOVE_EXPIRED ####
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench100RemoveExpired() {
		cache100.removeExpired();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench100RemoveExpiredOLD() {
//		cache100.removeExpiredOLD();
//	}
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void bench1000RemoveExpired() {
		cache1000.removeExpired();
	}

//	@GenerateMicroBenchmark
//	@BenchmarkMode(Mode.AverageTime)
//	public void bench1000RemoveExpiredOLD() {
//		cache1000.removeExpiredOLD();
//	}
	// #### REMOVE_EXPIRED ####
	// #### PUT_AND_GET ####
	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void benchCache100Instant() {
		for (int i = 0; i < 100; i++) {
			cacheEmpty.put(Integer.toString(i), Integer.toString(i));
			cacheEmpty.get(Integer.toString(i));
		}
		cacheEmpty.clear();
	}

	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void benchCache1000Instant() {
		for (int i = 0; i < 1000; i++) {
			cacheEmpty.put(Integer.toString(i), Integer.toString(i));
			cacheEmpty.get(Integer.toString(i));
		}
		cacheEmpty.clear();
	}

	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void benchCache100Seq() {
		for (int i = 0; i < 100; i++) {
			cacheEmpty.put(Integer.toString(i), Integer.toString(i));
		}
		for (int i = 0; i < 100; i++) {
			cacheEmpty.get(Integer.toString(i));
		}
		cacheEmpty.clear();
	}

	@GenerateMicroBenchmark
	@BenchmarkMode(Mode.AverageTime)
	public void benchCache1000Seq() {
		for (int i = 0; i < 1000; i++) {
			cacheEmpty.put(Integer.toString(i), Integer.toString(i));
		}
		for (int i = 0; i < 1000; i++) {
			cacheEmpty.get(Integer.toString(i));
		}
		cacheEmpty.clear();
	}
	// #### PUT_AND_GET ####
}
