package util.integration.rest.dto;

import java.io.Serializable;
import java.util.Date;

public class BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Date created;
	private Date lastModified;
	private boolean enabled;
	private String name;

	public BaseDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + (this.created != null ? this.created.hashCode() : 0);
		hash = 37 * hash + (this.lastModified != null ? this.lastModified.hashCode() : 0);
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
		final BaseDTO other = (BaseDTO) obj;
		if (this.created != other.created && (this.created == null || !this.created.equals(other.created))) {
			return false;
		}
		if (this.lastModified != other.lastModified && (this.lastModified == null || !this.lastModified.equals(other.lastModified))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BaseEntity{" + "created=" + created + ", lastModified=" + lastModified + '}';
	}
}
