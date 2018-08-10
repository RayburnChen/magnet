package org.nfa.panel.config;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.nfa.base.ApplicationException;
import org.nfa.base.ErrorDetail;
import org.nfa.base.ErrorResponse;
import org.nfa.base.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Integer DEFAULT_ERROR_CODE = 10000;
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private static final TypeReference<ErrorResponse> ERROR_RESPONSE_TYPE_REF = new TypeReference<ErrorResponse>() {
	};

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired(required = false)
	private MongoOperations mongoOperations;

	private String prefix;

	@PostConstruct
	public void init() {
		prefix = null == mongoOperations ? "EXCEPTION_LOG" : mongoOperations.getCollection(mongoOperations.getCollectionName(ExceptionLog.class)).getNamespace().getFullName();
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignException(FeignException e, HttpServletRequest request) {
		return handleException(e, Priority.MINOR, request, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(HystrixRuntimeException.class)
	public ResponseEntity<ErrorResponse> handleHystrixRuntimeException(HystrixRuntimeException e, HttpServletRequest request) {
		return handleException(e, Priority.MINOR, request, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<ErrorResponse> handleRestClientException(RestClientResponseException e, HttpServletRequest request) {
		return handleException(e, Priority.MINOR, request, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e, HttpServletRequest request) {
		return handleException(e, Priority.LOWEST, request, populate(e.getCode(), e.getMessage(), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErrorResponse> handleThrowable(Throwable e, HttpServletRequest request) {
		return handleException(e, Priority.HIGHEST, request, populate(DEFAULT_ERROR_CODE, initErrorMsg(e), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	private ResponseEntity<ErrorResponse> handleException(Throwable e, int priority, HttpServletRequest request, UnaryOperator<ErrorResponse> operator, ErrorResponse defaultErrorResponse) {
		log(e, request);
		Optional<ExceptionLog> exceptionLog = persist(initExceptionLog(e, request, priority, defaultErrorResponse));
		ErrorResponse errorResponse = populate(request, exceptionLog).andThen(operator).apply(defaultErrorResponse);
		return buildResponse(errorResponse);
	}

	private void log(Throwable e, HttpServletRequest request) {
		LOGGER.error("Error while processing " + request.getMethod() + ":" + request.getRequestURI(), e);
	}

	private Optional<ExceptionLog> persist(ExceptionLog exceptionLog) {
		if (null == mongoOperations || null == exceptionLog) {
			return Optional.empty();
		} else {
			mongoOperations.insert(exceptionLog);
			return Optional.of(exceptionLog);
		}
	}

	private ExceptionLog initExceptionLog(Throwable e, HttpServletRequest request, int priority, ErrorResponse nested) {
		ExceptionLog l = new ExceptionLog();
		l.setPath(request.getMethod() + ":" + request.getRequestURI());
		l.setCreatedDate(Instant.now());
		l.setPriority(priority);
		l.setDetails(nested.getDetails());
		l.setRootCause(nested.getException());
		initExceptionLog(l, e);
		return l;
	}

	private void initExceptionLog(ExceptionLog l, Throwable e) {
		l.setType(e.getClass().getName());
		l.setMessage(e.getMessage());
		l.setTrace(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
		if (null != e.getCause()) {
			ExceptionLog nested = new ExceptionLog();
			initExceptionLog(nested, e.getCause());
			l.setNested(nested);
		}
	}

	private UnaryOperator<ErrorResponse> populate(Integer code, String message, String exception) {
		return error -> {
			error.setCode(code);
			error.setMessage(message);
			error.setException(exception);
			return error;
		};
	}

	private UnaryOperator<ErrorResponse> populate(HttpServletRequest request, Optional<ExceptionLog> exceptionLog) {
		return error -> {
			error.setPath(request.getMethod() + " " + request.getRequestURI());
			exceptionLog.ifPresent(l -> error.getDetails().add(new ErrorDetail(prefix, l.getId())));
			return error;
		};
	}

	private ResponseEntity<ErrorResponse> buildResponse(ErrorResponse errorResponse) {
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	private String initErrorMsg(Throwable e) {
		StringJoiner sj = new StringJoiner(" ");
		sj.add(e.getClass().getSimpleName());
		sj.add(e.getMessage());
		sj.add(String.valueOf(e.getStackTrace()[0]));
		return sj.toString();
	}

	private ErrorResponse initNestedErrorResponse(HystrixRuntimeException exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof FeignException) {
			FeignException feignException = (FeignException) cause;
			return initNestedErrorResponse(feignException);
		} else {
			return defaultErrorResponse(exception);
		}
	}

	private ErrorResponse initNestedErrorResponse(FeignException exception) {
		return Optional.of(exception).map(FeignException::getMessage).filter(msg -> null != msg && msg.contains("; content:\n")).map(msg -> msg.split("; content:\n")[1]).map(this::initNested)
				.orElse(defaultErrorResponse(exception));
	}

	private ErrorResponse initNestedErrorResponse(RestClientResponseException exception) {
		return Optional.of(exception).map(RestClientResponseException::getResponseBodyAsString).map(this::initNested).orElse(defaultErrorResponse(exception));
	}

	private ErrorResponse initNested(String errorResponseString) {
		try {
			ErrorResponse errorResponse = objectMapper.readValue(errorResponseString, ERROR_RESPONSE_TYPE_REF);
			if (null == errorResponse.getMessage()) {
				errorResponse.setMessage(errorResponseString);
			}
			return errorResponse;
		} catch (RuntimeException | IOException e) {
			LOGGER.debug("Fetch error msg failed " + errorResponseString, e);
			return null;
		}
	}

	private ErrorResponse defaultErrorResponse(Throwable e) {
		return populate(DEFAULT_ERROR_CODE, e.getMessage(), e.getClass().getSimpleName()).apply(new ErrorResponse());
	}

	@Document(collection = "exception_log")
	public static class ExceptionLog implements Serializable {

		private static final long serialVersionUID = -1732166159902613935L;

		@Id
		private String id;
		@Field
		private String path;
		@Field
		private String userId;
		@Field
		private Instant createdDate;
		@Field
		private Integer priority;
		@Field
		private String type;
		@Field
		private List<ErrorDetail> details;
		@Field
		private String message;
		@Field
		private String rootCause;
		@Field
		private ExceptionLog nested;
		@Field
		private List<String> trace;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<String> getTrace() {
			return trace;
		}

		public void setTrace(List<String> trace) {
			this.trace = trace;
		}

		public Instant getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Instant createdDate) {
			this.createdDate = createdDate;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Integer getPriority() {
			return priority;
		}

		public void setPriority(Integer priority) {
			this.priority = priority;
		}

		public ExceptionLog getNested() {
			return nested;
		}

		public void setNested(ExceptionLog nested) {
			this.nested = nested;
		}

		public List<ErrorDetail> getDetails() {
			return details;
		}

		public void setDetails(List<ErrorDetail> details) {
			this.details = details;
		}

		public String getRootCause() {
			return rootCause;
		}

		public void setRootCause(String rootCause) {
			this.rootCause = rootCause;
		}

	}

}
