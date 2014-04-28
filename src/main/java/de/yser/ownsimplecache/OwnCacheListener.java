package de.yser.ownsimplecache;

import de.yser.ownsimplecache.util.CacheCommand;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Michael Koppen
 */
@MessageDriven(mappedName = "jms/CacheTopic", activationConfig = {
	@ActivationConfigProperty(propertyName = "acknowledgeMode",
		propertyValue = "Auto-acknowledge"),
	@ActivationConfigProperty(propertyName = "destinationType",
		propertyValue = "javax.jms.Topic"),
	@ActivationConfigProperty(propertyName = "destination",
		propertyValue = "cacheTopic")})
public class OwnCacheListener implements MessageListener {

	private static final Logger LOG = Logger.getLogger(OwnCacheListener.class.getName());
	@Resource
	private MessageDrivenContext mdc;
	@EJB
	private OwnCacheService cache;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {

				TextMessage textMessage = (TextMessage) message;
				LOG.log(Level.INFO, "REST_CACHE");
				LOG.log(Level.INFO, "Received TextMessage: {0}", textMessage);
				LOG.log(Level.INFO, "Command: {0}", textMessage.getStringProperty("command"));
				LOG.log(Level.INFO, "sent: {0}", new Date(textMessage.getLongProperty("sent")));
				LOG.log(Level.INFO, "received: {0}", new Date(System.currentTimeMillis()));
				if (textMessage.getStringProperty("command").equals(CacheCommand.INVALIDATE_ALL_CACHES.name())) {
					cache.invalidateAllCaches();
				}
				if (textMessage.getStringProperty("command").equals(CacheCommand.INVALIDATE_CACHE.name())) {
					LOG.log(Level.INFO, "genericTypeHint: {0}", textMessage.getStringProperty("genericTypeHint"));
					cache.invalidateCache(textMessage.getStringProperty("type"), textMessage.getStringProperty("genericTypeHint"));
				}
			} catch (JMSException ex) {
				Logger.getLogger(OwnCacheListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
