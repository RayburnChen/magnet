package org.nfa.common;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -6804756411503521941L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public ApplicationException(String message, Integer code, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ApplicationException(Integer code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
