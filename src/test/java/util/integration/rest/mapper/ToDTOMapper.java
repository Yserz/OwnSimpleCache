package util.integration.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import util.integration.domain.Category;
import util.integration.rest.dto.CategoryDTO;

/**
 *
 * @author Michael Koppen
 */
public class ToDTOMapper {

	private boolean checkDept(int dept) {
		return dept <= 0;
	}

	private CategoryDTO mapCategoryDept(Category e, int dept) {
		if (checkDept(dept)) {
			return null;
		}
		dept--;
		CategoryDTO dto = new CategoryDTO();

		dto.setId(e.getId());
		dto.setName(e.getName());
		dto.setSlug(e.getSlug());
		dto.setColor(e.getColor());
		dto.setCreated(e.getCreated());
		dto.setLastModified(e.getLastModified());

		return dto;
	}

	private List<CategoryDTO> mapCategoryListDept(List<Category> eList, int dept) {
		if (checkDept(dept)) {
			return null;
		}
		List<CategoryDTO> dtoList = new ArrayList<>();
		for (Category e : eList) {
			dtoList.add(mapCategoryDept(e, dept));
		}

		return dtoList;
	}

	// PUBLISHED METHODS
	public CategoryDTO mapCategory(Category e) {
		return mapCategoryDept(e, 1);
	}

	public List<CategoryDTO> mapCategoryList(List<Category> eList) {

		List<CategoryDTO> dtoList = new ArrayList<>();
		for (Category e : eList) {
			dtoList.add(mapCategory(e));
		}

		return dtoList;
	}

}
