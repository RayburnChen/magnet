package org.nfa.base;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

	private String path;
	private String message;
	private Integer code;
	private String exception;
	private Instant timestamp = Instant.now();
	private List<ErrorDetail> details = new ArrayList<>();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public ErrorResponse setCode(Integer code) {
		this.code = code;
		return this;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public List<ErrorDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ErrorDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "ErrorResponse [path=" + path + ", message=" + message + ", code=" + code + ", exception=" + exception + ", timestamp=" + timestamp + ", details=" + details + "]";
	}

}
