package org.nfa.base;

public class ErrorDetail {

	private String name;
	private String message;

	public ErrorDetail(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public ErrorDetail() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
