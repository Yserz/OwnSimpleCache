package de.yser.ownsimplecache.util.jaxrs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *
 * @author Michael Koppen
 */
public class EntityTagGenerator {

	private Response.ResponseBuilder crb;
	private CacheControl cc;
	private static final Logger LOG = Logger.getLogger(EntityTagGenerator.class.getName());

	public EntityTagGenerator() {

		this.crb = null;
		this.cc = new CacheControl();
		//Set max age to one day
		this.cc.setMaxAge(86400);
	}

	Response generateTaggedResponse(Response resp) {
		resp = Response.ok(resp.getEntity()).cacheControl(cc).tag(generateEntityTag(resp)).build();
		return resp;
	}

	Response generateTaggedResponseOrNotModified(Response resp, Request req) {
		EntityTag etag = generateEntityTag(resp);

		resp = generateTaggedResponse(resp);
		//Verify if it matched with etag available in http request
		crb = null;
		crb = req.evaluatePreconditions(etag);
		//If ETag matches the rb will be non-null
		if (crb != null) {
			//return HTTP 304 header [Not Modified]
			LOG.log(Level.INFO, "ETAG-GEN\t: -\t\t\tEntity not modified, returning 304.");
			resp = crb.cacheControl(cc).tag(etag).build();
		}

		return resp;
	}

	private EntityTag generateEntityTag(Response resp) {
		EntityTag etag = null;
		LOG.log(Level.INFO, "ETAG-GEN\t: +\t\tGenerating EntityTag");
		Object entity = resp.getEntity();
		etag = new EntityTag(entity.hashCode() + "");

		return etag;
	}
}
