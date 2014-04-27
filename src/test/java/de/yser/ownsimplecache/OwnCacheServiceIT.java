package de.yser.ownsimplecache;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jayway.restassured.module.jsv.JsonSchemaValidator;
import com.jayway.restassured.response.Response;
import de.yser.ownsimplecache.util.jaxrs.CacheInterceptor;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import org.junit.rules.TestName;
import util.GFInstance;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV3;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.jayway.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Michael Koppen
 */
public class OwnCacheServiceIT {

	@Rule
	// Get name of actual Test with test.getMethodName()
	public TestName test = new TestName();

	private static final String HOST = "http://localhost";
	private static final String PORT = "9090";
	private static final String ROOT = HOST + ":" + PORT + "/" + GFInstance.APP_NAME;

	private static final String CATEGORY_LIST_SCHEMA = "json-schema/categoryList-schema.json";

	@BeforeClass
	public static void setUpClass() throws Exception {

		JsonSchemaValidator.settings = settings().with().jsonSchemaFactory(
			JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV3).freeze()).freeze()).
			and().with().checkedValidation(false);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		GFInstance.deployer.deploy(GFInstance.archive.toURI(), "--contextroot=" + GFInstance.APP_NAME, "--force=true");
	}

	@After
	public void tearDown() throws Exception {
		GFInstance.deployer.undeploy(GFInstance.APP_NAME);
	}

	/**
	 * Test of cache method, of class OwnCacheService.
	 */
	@Test
	public void testRestResource() throws Exception {
		Logger.getLogger(CacheInterceptor.class.getName()).setLevel(Level.ALL);

		Response resp1 = given().when().get(ROOT + "/service/v1/categories").then().extract().response();

		resp1.then().assertThat().body(matchesJsonSchema(getSchema(CATEGORY_LIST_SCHEMA)));
		System.out.println("RESPONSE: ");
		resp1.prettyPrint();

		get(ROOT + "/service/v1/categories/add");

		Response resp2 = given().when().get(ROOT + "/service/v1/categories").then().extract().response();

		resp2.then().assertThat().body(matchesJsonSchema(getSchema(CATEGORY_LIST_SCHEMA)));
		System.out.println("RESPONSE: ");
		resp2.prettyPrint();
		resp2.then().assertThat().body(not(containsString("[]")));
	}

	private static InputStream getSchema(String schemaPath) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaPath);
	}

}
