package util.integration.rest.resource;

import de.yser.ownsimplecache.util.jaxrs.RESTCache;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import util.integration.domain.Category;
import util.integration.rest.dto.CategoryDTO;
import util.integration.rest.mapper.ToDTOMapper;
import util.integration.service.CategoryService;

/**
 *
 * @author MacYser
 */
@Path("categories")
@Stateless
public class CategoryResource {

	private static final Logger LOG = Logger.getLogger(CategoryResource.class.getName());

	@EJB
	private CategoryService categoryService;
	private final ToDTOMapper toDTOMapper;

	public CategoryResource() {
		toDTOMapper = new ToDTOMapper();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("ping")
	public String ping() {
		return "pong";
	}

	@RESTCache
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategories(@Context Request req, @Context UriInfo uriInfo) {

		Response resp;
		try {

			List<Category> cat = categoryService.getCategories();
			Collection<CategoryDTO> categories = toDTOMapper.mapCategoryList(cat);

			resp = Response.ok(categories).build();
		} catch (Exception e) {
			LOG.log(Level.INFO, "Exception: {0}", e.getMessage());
			resp = Response.status(500).build();
		}

		return resp;
	}

	@RESTCache(genericTypeHint = "de.yser.ownsimplecacheexample.rest.dto.CategoryDTO")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("list")
	public List<CategoryDTO> getCategoryList(@Context Request req) {

		List<Category> cat = categoryService.getCategories();
		List<CategoryDTO> categories = toDTOMapper.mapCategoryList(cat);

		return categories;
	}

	@RESTCache
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("count")
	public int count() {
		return categoryService.count();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add")
	public boolean add() {
		return categoryService.add(new Category(((Double) Math.random()).longValue(), "#000000", "slug"));
	}

	@RESTCache
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Category get(@PathParam("id") int index) {
		return categoryService.get(index);
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Category remove(@PathParam("id") int index) {
		return categoryService.remove(index);
	}
}
