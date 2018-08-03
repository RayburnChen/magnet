package org.nfa.common;

import java.io.Serializable;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2000862699355091779L;

	@Id
	private String id;
	@Field
	private boolean enabled;
	@Version
	private Long version;
	@CreatedBy
	private AuditableUser createdBy;
	@LastModifiedBy
	private AuditableUser lastModifiedBy;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public AuditableUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AuditableUser createdBy) {
		this.createdBy = createdBy;
	}

	public AuditableUser getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(AuditableUser lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BaseEntity [id=" + id + ", enabled=" + enabled + "]";
	}

}
