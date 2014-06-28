package de.yser.ownsimplecache;

import de.yser.ownsimplecache.util.CacheCommand;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.*;

/**
 *
 * @author Michael Koppen
 */
@Singleton
public class OwnCacheServerService {

	private static final Logger LOG = Logger.getLogger(OwnCacheServerService.class.getName());

	@Resource(mappedName = "java:app/jms/CacheTopicConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:app/jms/CacheTopic")
	private Topic cacheTopic;
//	private Map<String, Object> cacheServices;

	public OwnCacheServerService() {
		LOG.log(Level.INFO, "OwnCacheServerService startup....");
	}

	@PostConstruct
	private void init() {
		invalidateAllCaches();

	}

	public static void registerHooks(Class<?> hookType, Class<?>... hooks) throws Exception {
		OwnCacheService.registerHooks(hookType, hooks);
	}

	public void invalidateAllCaches() {
		LOG.log(Level.INFO, "INVALIDATING ALL CACHES");
		try {
			Connection connection = null;
			Session session = null;

			try {
				connection = connectionFactory.createConnection();
				connection.start();

				// Create a Session
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create a MessageProducer from the Session to the Topic or Queue
				MessageProducer producer = session.createProducer(cacheTopic);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				// Create a message
				TextMessage message = session.createTextMessage(CacheCommand.INVALIDATE_ALL_CACHES.name());
				message.setStringProperty("command", CacheCommand.INVALIDATE_ALL_CACHES.name());
				message.setLongProperty("sent", System.currentTimeMillis());

				// Tell the producer to send the message
				producer.send(message);

			} finally {
				// Clean up
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		} catch (JMSException ex) {
			Logger.getLogger(OwnCacheService.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void invalidateCache(String cacheType, String genericTypeHint) {
		LOG.log(Level.INFO, "INVALIDATING CACHE OF TYPE {0}", cacheType);
		try {
			Connection connection = null;
			Session session = null;

			try {
				connection = connectionFactory.createConnection();
				connection.start();

				// Create a Session
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create a MessageProducer from the Session to the Topic or Queue
				MessageProducer producer = session.createProducer(cacheTopic);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				// Create a message
				TextMessage message = session.createTextMessage(CacheCommand.INVALIDATE_CACHE.name());
				message.setStringProperty("command", CacheCommand.INVALIDATE_CACHE.name());
				if (genericTypeHint != null) {
					message.setStringProperty("genericTypeHint", genericTypeHint);
				}
				message.setStringProperty("type", cacheType);

				message.setLongProperty("sent", System.currentTimeMillis());

				// Tell the producer to send the message
				producer.send(message);

			} finally {
				// Clean up
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		} catch (JMSException ex) {
			Logger.getLogger(OwnCacheService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
