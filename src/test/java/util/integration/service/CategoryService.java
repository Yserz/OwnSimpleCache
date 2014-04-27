package util.integration.service;

import de.yser.ownsimplecache.util.clear.ClearCache;
import de.yser.ownsimplecache.util.clear.ClearCaches;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.integration.domain.Category;
import util.integration.provider.CategoryProvider;

/**
 *
 * @author Michael Koppen
 */
@Stateless
public class CategoryService {

	@EJB
	private CategoryProvider provider;

	public List<Category> getCategories() {
		return provider.getAll();
	}

	public int count() {
		return provider.count();
	}

	// Java <= 7
	@ClearCaches({
		@ClearCache(
			cacheType = "javax.ws.rs.core.Response"),
		@ClearCache(
			cacheType = "java.util.List",
			genericTypeHint = "util.integration.rest.dto.CategoryDTO")
	})
	public boolean add(Category e) {
		return provider.add(e);
	}

	public Category get(int index) {
		return provider.get(index);
	}

	// Java 8 Repeatable Annotations
//	@ClearCache(
//		cacheType = "javax.ws.rs.core.Response")
//	@ClearCache(
//		cacheType = "java.util.List",
//		genericTypeHint = "util.integration.rest.dto.CategoryDTO")
	// Java <= 7
	@ClearCaches({
		@ClearCache(
			cacheType = "javax.ws.rs.core.Response"),
		@ClearCache(
			cacheType = "java.util.List",
			genericTypeHint = "util.integration.rest.dto.CategoryDTO")
	})
	public Category remove(int index) {
		return provider.remove(index);
	}
}
