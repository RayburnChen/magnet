package org.nfa.athena.controller;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;

import org.nfa.athena.model.User;
import org.nfa.athena.model.UserDTO;
import org.nfa.athena.service.AthenaService;
import org.nfa.base.model.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import brave.Tracer;

@RestController
@RequestMapping(value = "/greeting")
@Validated
public class AthenaController {

	private static final Logger log = LoggerFactory.getLogger(AthenaController.class);

	@Autowired
	private AthenaService athenaService;
	@Autowired
	private Tracer tracer;
	@Autowired
	private BeanFactory beanFactory;// DefaultListableBeanFactory

	@PostConstruct
	public void init() {
		log.info("BeanFactory class is {}", beanFactory.getClass().getName());
	}

	@GetMapping("/oneUser")
	public User oneUser(@RequestHeader HttpHeaders headers, UserDTO user) {
		String span = tracer.currentSpan().context().toString();
		log.info("HttpHeaders {}, User {}", headers, user);
		log.info("Tracer currentSpan is {}", span);
		return athenaService.oneUser();
	}

	@GetMapping("/user")
	public User user(@RequestParam(value = "id") String id) {
		return athenaService.findOne(id);
	}

	@PostMapping("/user")
	public User insertUser(@RequestBody @Validated UserDTO userDTO) {
		log.info("insertUser {}", userDTO);
		User user = new User();
		user.setName(userDTO.getName());
		return athenaService.insert(user);
	}

	@GetMapping("/userByName")
	public User userByName(@RequestParam("name") String name, @RequestParam(value = "age", required = false) @Max(10) Integer age) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		log.info(requestAttributes.getRequest().getServletPath());
		log.info("oneUserByName {}", name);
		return athenaService.findOneByName(name);
	}

	@GetMapping("/userByName/{name}")
	public User userByNamePath(@PathVariable("name") String name) {
		log.info("oneUserByNamePath {}", name);
		return athenaService.findOneByName(name);
	}

	@GetMapping("/exception")
	public User exception() {
		log.info("User exception");
		throw new ApplicationException("My exception Assert.isNull", 0);
	}

	@GetMapping("/sse")
	public void push(HttpServletResponse response) throws NoSuchAlgorithmException {
		// curl localhost:8110/greeting/sse
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("utf-8");
		Random r = SecureRandom.getInstanceStrong();
		while (true) {
			try {
				Thread.sleep(1000);
				PrintWriter pw = response.getWriter();
				if (pw.checkError()) {
					log.info("Connnection Stopped");
					return;
				}
				pw.write("data:Testing 1,2,3" + r.nextInt() + "\n\n");
				pw.flush();
			} catch (Exception e) {
				log.error("sleep failed", e);
			}
		}

	}
}
