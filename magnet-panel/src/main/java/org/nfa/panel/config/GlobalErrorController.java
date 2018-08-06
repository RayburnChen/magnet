package org.nfa.panel.config;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractErrorController {

	private static final Logger log = LoggerFactory.getLogger(GlobalErrorController.class);

	@Autowired
	private ServerProperties serverProperties;
	private ErrorAttributes errorAttributes;

	public GlobalErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return this.serverProperties.getError().getPath();
	}

	@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		HttpStatus status = getStatus(request);
		String origialUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		Map<String, Object> body = errorAttributes.getErrorAttributes(requestAttributes, true);
		log.error("GlobalErrorController. HttpStatus {} {} {}", status, origialUri, body);
		throw new RuntimeException(origialUri + " " + String.valueOf(body.get("error")), errorAttributes.getError(requestAttributes));
	}

}
