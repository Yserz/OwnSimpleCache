package de.yser.ownsimplecache.util.hook;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michael Koppen
 */
public interface Hook {

	public void willCache(String objectOfClass, String withKey, String andValue);

	public void didCache(String objectOfClass, String withKey, String andValue);

	public void willGet(String objectOfClass, String withKey);

	public void didGet(String objectOfClass, String withKey);

	public void willInvalidateCache(String ofClass, String withHint);

	public void didInvalidateCache(String ofClass, String withHint);

	public void willInvalidateAllCaches();

	public void didInvalidateAllCaches();

	public void willCreateCache(String name, String genericTypeHint, long expiringTime, TimeUnit unit);

	public void didCreateCache(String name, String genericTypeHint, long expiringTime, TimeUnit unit);
}
