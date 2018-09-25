package org.nfa.base.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.nfa.base.model.ApplicationException;
import org.nfa.base.model.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.mongodb.client.result.DeleteResult;

@RestController
@RequestMapping(value = "/operation")
public class OperationController {

	private static final Logger log = LoggerFactory.getLogger(OperationController.class);

	@Autowired(required = false)
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MongoOperations mongoOperations;
	@Value("${logging.file}")
	private String path;

	@RequestMapping(method = RequestMethod.GET, value = { "/encode" })
	public String encode(@RequestParam String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/matches" })
	public boolean matches(@RequestParam String rawPassword, @RequestParam String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = { "/collection/drop" })
	public void dropCollection(@RequestParam String collectionName) {
		mongoOperations.dropCollection(collectionName);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = { "/collection/remove" })
	public DeleteResult removeCollection(@RequestParam String collectionName) {
		return mongoOperations.remove(new Query(), collectionName);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/collection/name" })
	public Holder<String> getCollection() {
		return new Holder<String>(mongoOperations.getCollectionNames().stream().collect(Collectors.toList()));
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/collection" })
	public Holder<Object> getCollection(@RequestParam String collectionName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(new Order(Direction.DESC, "createdBy.time")));
		return new Holder<Object>(PageableExecutionUtils.getPage(mongoOperations.find(new Query().with(pageable), Object.class, collectionName), pageable,
				() -> mongoOperations.count(new Query(), collectionName)));
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/file" })
	public Holder<Path> searchFile(HttpServletRequest request, @RequestParam(required = false) String path) throws IOException {
		Path root = StringUtils.isBlank(path) ? Paths.get(System.getProperty("user.dir")) : Paths.get(URI.create(path));
		List<Path> paths = new ArrayList<>();
		Files.walkFileTree(root, getFileVisitor(paths));
		log.info("paths: ", paths);
		return new Holder<Path>(paths);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/file/download" })
	public StreamingResponseBody downloadFile(HttpServletResponse response, @RequestParam String path) throws IOException {
		Path filePath = Paths.get(URI.create(path));
		String fileName = filePath.getFileName().toString();
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setContentType("application/octet-stream;charset=UTF-8");
		return outputStream -> outputStream.write(Files.readAllBytes(filePath));
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/logging" })
	public Holder<Path> getLoggingPath() throws IOException {
		if (StringUtils.isBlank(path)) {
			throw new ApplicationException("logging.path is blank", 10000);
		}
		Path loggingPath = Paths.get(path).getParent();
		List<Path> paths = new ArrayList<>();
		Files.walkFileTree(loggingPath, getFileVisitor(paths));
		log.info("paths: ", paths);
		return new Holder<Path>(paths);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = { "/logging" })
	public Holder<Path> purgeLoggingPath() throws IOException {
		if (StringUtils.isBlank(path)) {
			throw new ApplicationException("logging.path is blank", 10000);
		}
		Path loggingPath = Paths.get(path).getParent();
		List<Path> paths = new ArrayList<>();
		Files.walkFileTree(loggingPath, deleteFileVisitor(paths));
		log.info("paths: ", paths);
		return new Holder<Path>(paths);
	}

	private FileVisitor<Path> getFileVisitor(List<Path> paths) {
		return new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				paths.add(file);
				return FileVisitResult.CONTINUE;
			}
		};
	}

	private FileVisitor<Path> deleteFileVisitor(List<Path> paths) {
		return new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				try {
					paths.add(file);
					Files.delete(file);
				} catch (IOException e) {
					log.warn("Files.delete failed", e);
				}
				return FileVisitResult.CONTINUE;
			}
		};
	}

}
