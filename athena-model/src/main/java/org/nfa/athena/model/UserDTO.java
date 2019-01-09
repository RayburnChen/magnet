package org.nfa.athena.model;

import javax.validation.constraints.NotNull;

public class UserDTO {

	@NotNull(message = "name is missing")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
