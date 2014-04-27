package util.integration.rest.dto;

import java.util.Objects;

/**
 *
 * @author Benny
 */
public class CategoryDTO extends BaseDTO {

	private String slug;
	private String color;

	public CategoryDTO() {
	}

	public CategoryDTO(String slug, String color) {
		this.slug = slug;
		this.color = color;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + Objects.hashCode(this.slug);
		hash = 11 * hash + Objects.hashCode(this.color);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CategoryDTO other = (CategoryDTO) obj;
		if (!Objects.equals(this.getId(), other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CategoryDTO{" + "id=" + getId() + '}';
	}
}
