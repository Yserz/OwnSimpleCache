package util.integration.rest;

import javax.annotation.PostConstruct;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/service/v1")
public class RestConfig extends ResourceConfig {

	public RestConfig() {
		// RESOURCES
		packages("util.integration.rest.resource", "com.fasterxml.jackson.jaxrs.base");

		// MOXy JSON
//		register(MoxyJsonFeature.class);
		property(JsonGenerator.PRETTY_PRINTING, true);
//		property(MarshallerProperties.JSON_NAMESPACE_SEPARATOR, ".");
		property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
		// MOXy XML
//		register(MoxyXmlFeature.class);
//		property(MessageProperties.XML_FORMAT_OUTPUT, true);
		// LOGGING
		register(LoggingFilter.class);
//		property(ServerProperties.TRACING, "ON_DEMAND");
		// Enable monitoring MBeans
//		property(ServerProperties.MONITORING_STATISTICS_MBEANS_ENABLED, true);
//		property(ServerProperties.MONITORING_STATISTICS_ENABLED, true);
//		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

//		// Enable support for role-based authorization
//		register(RolesAllowedDynamicFeature.class);
//
//		// Enable JSON entity filtering
//		register(EntityFilteringFeature.class);
	}

	@PostConstruct
	private void init() {
	}

}
