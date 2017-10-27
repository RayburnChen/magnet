package org.nfa.athena;

public enum UserType {

	ADMIN("normal", "This is admin"), STAFF("normal", "this is staff");
	private String tag;
	private String detail;

	private UserType(String tag, String detail) {
		this.tag = tag;
		this.detail = detail;
	}

	public String getTag() {
		return tag;
	}

	public String getDetail() {
		return detail;
	}

}
