package org.nfa.atropos.controller;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.nfa.atropos.model.Article;
import org.nfa.atropos.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@RestController
@RequestMapping("/article")
public class ArticleController {

	private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;

	@GetMapping
	public Flux<Article> list(@RequestHeader HttpHeaders httpHeaders) {
		log.info("HttpHeaders " + httpHeaders.getContentType());
		return articleService.list();
	}

	@GetMapping("/all")
	public Flux<Article> getArticleByIds(@RequestParam List<String> ids) {
		log.info("getArticleById {}", ids);
		return articleService.getById(Flux.fromIterable(ids));
	}

	@GetMapping("/{id}")
	public Mono<Article> getById(@PathVariable("id") String id) {
		return articleService.getById(id);
	}

	@PostMapping
	public Mono<Article> create(@RequestBody Article article) {
		return articleService.createOrUpdate(article);
	}

	@PutMapping("/{id}")
	public Mono<Article> update(@PathVariable("id") String id, @RequestBody Article article) {
		Objects.requireNonNull(article);
		article.setId(id);
		return articleService.createOrUpdate(article);
	}

	@DeleteMapping("/{id}")
	public Mono<Article> delete(@PathVariable("id") String id) {
		return articleService.delete(id);
	}

	@GetMapping(value = "/randomNumbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Integer>> randomNumbers() {
		// curl localhost:8120/article/randomNumbers
		return Flux.interval(Duration.ofSeconds(1)).log().map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt())).map(data -> {
			log.info("Thread:" + Thread.currentThread().toString());
			return ServerSentEvent.<Integer>builder().event("random").id(Long.toString(data.getT1())).data(data.getT2()).build();
		});
	}

	@GetMapping(value = "/randomArticles", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Article> randomArticles() {
		// curl localhost:8120/article/randomNumbers
		return Flux.interval(Duration.ofSeconds(1)).log().map(data -> {
			log.info("Thread:" + Thread.currentThread().toString());
			Article article = new Article();
			article.setId(String.valueOf(ThreadLocalRandom.current().nextLong()));
			return article;
		});
	}

}
