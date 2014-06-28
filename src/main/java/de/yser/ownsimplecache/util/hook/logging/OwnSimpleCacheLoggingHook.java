package de.yser.ownsimplecache.util.hook.logging;

import de.yser.ownsimplecache.util.hook.Hook;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Koppen
 */
public class OwnSimpleCacheLoggingHook implements Hook {

	private static final Logger LOG = Logger.getLogger(OwnSimpleCacheLoggingHook.class.getName());

	@Override
	public void willCache(String objectOfClass, String withKey, String andValue) {
		LOG.log(Level.INFO, "willCache object of class \"{0}\" with key \"{1}\" and value \"{2}\".", new Object[]{objectOfClass, withKey, andValue});
	}

	@Override
	public void didCache(String objectOfClass, String withKey, String andValue) {
		LOG.log(Level.INFO, "didCache object of class \"{0}\" with key \"{1}\" and value \"{2}\".", new Object[]{objectOfClass, withKey, andValue});
	}

	@Override
	public void willGet(String objectOfClass, String withKey) {
		LOG.log(Level.INFO, "willGet object of class \"{0}\" with key \"{1}\".", new Object[]{objectOfClass, withKey});
	}

	@Override
	public void didGet(String objectOfClass, String withKey) {
		LOG.log(Level.INFO, "didGet object of class \"{0}\" with key \"{1}\".", new Object[]{objectOfClass, withKey});
	}

	@Override
	public void willInvalidateCache(String ofClass, String withHint) {
		LOG.log(Level.INFO, "willInvalidateCache of class \"{0}\" with hint \"{1}\".", new Object[]{ofClass, withHint});
	}

	@Override
	public void didInvalidateCache(String ofClass, String withHint) {
		LOG.log(Level.INFO, "didInvalidateCache of class \"{0}\" with hint \"{1}\".", new Object[]{ofClass, withHint});
	}

	@Override
	public void willInvalidateAllCaches() {
		LOG.log(Level.INFO, "willInvalidateAllCaches.");
	}

	@Override
	public void didInvalidateAllCaches() {
		LOG.log(Level.INFO, "didInvalidateAllCaches.");
	}

	@Override
	public void willCreateCache(String name, String genericTypeHint, long expiringTime, TimeUnit unit) {
		LOG.log(Level.INFO, "willCreateCache \"{0}\" with hint \"{1}\" and expiring time \"{2} {3}\".", new Object[]{name, genericTypeHint, expiringTime, unit.name()});
	}

	@Override
	public void didCreateCache(String name, String genericTypeHint, long expiringTime, TimeUnit unit) {
		LOG.log(Level.INFO, "didCreateCache \"{0}\" with hint \"{1}\" and expiring time \"{2} {3}\".", new Object[]{name, genericTypeHint, expiringTime, unit.name()});
	}

}
