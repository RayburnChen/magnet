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
import javax.validation.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.nfa.base.ApplicationException;
import org.nfa.base.ErrorDetail;
import org.nfa.base.ErrorResponse;
import org.nfa.base.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private Environment environment;

	private String prefix;

	@PostConstruct
	public void init() {
		prefix = null == environment.getProperty("spring.application.name") ? "EXCEPTION_LOG" : environment.getProperty("spring.application.name");
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
		return handleException(e, Priority.MINOR, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(HystrixRuntimeException.class)
	public ResponseEntity<ErrorResponse> handleHystrixRuntimeException(HystrixRuntimeException e) {
		return handleException(e, Priority.MINOR, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<ErrorResponse> handleRestClientException(RestClientResponseException e) {
		return handleException(e, Priority.MINOR, UnaryOperator.identity(), initNestedErrorResponse(e));
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
		return handleException(e, Priority.LOWEST, populate(e.getCode(), e.getMessage(), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return handleException(e, Priority.LOWEST, populate(DEFAULT_ERROR_CODE, initErrorMsg(e), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
		return handleException(e, Priority.LOWEST, populate(DEFAULT_ERROR_CODE, e.getMessage(), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErrorResponse> handleThrowable(Throwable e) {
		return handleException(e, Priority.HIGHEST, populate(DEFAULT_ERROR_CODE, initErrorMsg(e), e.getClass().getSimpleName()), defaultErrorResponse(e));
	}

	private ResponseEntity<ErrorResponse> handleException(Throwable e, int priority, UnaryOperator<ErrorResponse> operator, ErrorResponse defaultErrorResponse) {
		log(e);
		Optional<ExceptionLog> exceptionLog = persist(initExceptionLog(e, priority, defaultErrorResponse));
		ErrorResponse errorResponse = populate(exceptionLog).andThen(operator).apply(defaultErrorResponse);
		return buildResponse(errorResponse);
	}

	private void log(Throwable e) {
		LOGGER.error("Error while processing " + getRequestPath(), e);
	}

	private Optional<ExceptionLog> persist(ExceptionLog exceptionLog) {
		if (null == mongoOperations || null == exceptionLog) {
			return Optional.empty();
		} else {
			mongoOperations.insert(exceptionLog);
			return Optional.of(exceptionLog);
		}
	}

	private ExceptionLog initExceptionLog(Throwable e, int priority, ErrorResponse nested) {
		ExceptionLog l = new ExceptionLog();
		initExceptionLog(l, e);
		l.setPath(getRequestPath());
		l.setCreatedDate(Instant.now());
		l.setPriority(priority);
		l.setDetails(nested.getDetails());
		l.setRootCause(nested.getException());
		l.setMessage(nested.getMessage());
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

	private UnaryOperator<ErrorResponse> populate(Optional<ExceptionLog> exceptionLog) {
		return error -> {
			error.setPath(getRequestPath());
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

	private String initErrorMsg(MethodArgumentNotValidException e) {
		return Optional.of(e).map(MethodArgumentNotValidException::getBindingResult).map(BindingResult::getAllErrors).filter(CollectionUtils::isNotEmpty).map(l -> l.get(0))
				.map(ObjectError::getDefaultMessage).orElse(e.getMessage());
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
		return Optional.of(exception).map(FeignException::getMessage).filter(msg -> null != msg && msg.contains("; content:\n")).map(msg -> msg.split("; content:\n")[1])
				.map(this::initNested).orElse(defaultErrorResponse(exception));
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

	private String getRequestPath() {
		if (null == request) {
			return null;
		} else {
			return request.getMethod() + ":" + request.getRequestURI();
		}
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
