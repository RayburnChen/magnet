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
import java.util.Scanner;
import java.util.function.Function;
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
import org.springframework.boot.logging.LogFile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
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
@RequestMapping(value = "/management")
public class ManagementController {

	private static final Logger log = LoggerFactory.getLogger(ManagementController.class);
	private final String sep = System.getProperty("line.separator");

	@Autowired(required = false)
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MongoOperations mongoOperations;
	@Value("${logging.file}")
	private String path;
	@Autowired
	private Environment environment;

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
	public Holder<Object> getCollection(@RequestParam String collectionName, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(new Order(Direction.DESC, "createdBy.time")));
		return new Holder<Object>(PageableExecutionUtils.getPage(
				mongoOperations.find(new Query().with(pageable), Object.class, collectionName), pageable,
				() -> mongoOperations.count(new Query(), collectionName)));
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/file" })
	public Holder<Path> searchFile(HttpServletRequest request, @RequestParam(required = false) String path)
			throws IOException {
		Path root = StringUtils.isBlank(path) ? Paths.get(System.getProperty("user.dir")) : Paths.get(URI.create(path));
		List<Path> paths = new ArrayList<>();
		Files.walkFileTree(root, getFileVisitor(paths));
		log.info("paths: ", paths);
		return new Holder<Path>(paths);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/file/download" })
	public StreamingResponseBody downloadFile(HttpServletResponse response, @RequestParam String path)
			throws IOException {
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

	@RequestMapping(method = RequestMethod.GET, value = { "/logging/block" })
	public String loggingBlock(@RequestParam String pattern, @RequestParam(required = false) String thread,
			@RequestParam(defaultValue = "1000") int limit, @RequestParam(defaultValue = "0") int skip) {
		return scan(scanner -> searchBlock(scanner, pattern, thread, limit, skip));
	}

	private String searchBlock(Scanner scanner, String pattern, String thread, int limit, int skip) {
		StringBuilder builder = new StringBuilder();
		int s = skip < 0 ? 0 : skip;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (hit(line, pattern, thread)) {
				if (0 == s) {
					builder.append(line);
					builder.append(sep);
					break;
				} else {
					s = s - 1;
				}
			}
		}
		if (0 != s) {
			return builder.toString();
		}
		int l = limit;
		while (scanner.hasNextLine() && l > 0) {
			String line = scanner.nextLine();
			if (StringUtils.isBlank(thread) || line.contains(thread)) {
				builder.append(line);
				builder.append(sep);
				l = l - 1;
			}
		}
		return builder.toString();
	}

	private boolean hit(String line, String pattern, String thread) {
		boolean contains = line.contains(pattern);
		if (!contains) {
			return false;
		}
		if (StringUtils.isBlank(thread)) {
			return true;
		}
		return line.contains(thread);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/logging/search" })
	public String loggingSearch(@RequestParam String pattern) {
		return scan(scanner -> searchAll(scanner, pattern));
	}

	private String searchAll(Scanner scanner, String pattern) {
		StringBuilder builder = new StringBuilder();
		int row = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains(pattern)) {
				builder.append("[" + row + "]");
				builder.append(line);
				builder.append(sep);
				row = row + 1;
			}
		}
		return builder.toString();
	}

	private <T> T scan(Function<Scanner, T> function) {
		LogFile logFile = LogFile.get(this.environment);
		if (logFile == null) {
			log.debug("Missing 'logging.file' or 'logging.path' properties");
			return null;
		}
		FileSystemResource resource = new FileSystemResource(logFile.toString());
		try {
			Scanner scanner = new Scanner(resource.getInputStream(), "UTF-8");
			return function.apply(scanner);
		} catch (IOException e) {
			log.error("Failed to create Scanner", e);
			return null;
		}
	}

}
