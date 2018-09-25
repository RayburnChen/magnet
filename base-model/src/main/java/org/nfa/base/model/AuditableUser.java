package org.nfa.base.model;

import java.time.Instant;

public class AuditableUser {

	public AuditableUser() {
		super();
	}

	public AuditableUser(String name, String roles) {
		super();
		this.name = name;
		this.roles = roles;
	}

	private String name;
	private Instant time = Instant.now();
	private String roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "AuditableUser [name=" + name + ", time=" + time + ", roles=" + roles + "]";
	}

}
