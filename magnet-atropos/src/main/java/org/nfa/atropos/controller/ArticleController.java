package org.nfa.atropos.controller;

import org.nfa.atropos.model.Article;
import org.nfa.atropos.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/article")
public class ArticleController {

	private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;

	@GetMapping
	public Mono<Article> getArticleById(@RequestParam String id) {
		log.info("getArticleById {}", id);
		return articleService.getById(id);
	}

	@GetMapping("/all")
	public Flux<Article> getArticleByIds(Flux<String> ids) {
		log.info("getArticleById {}", ids);
		return articleService.getById(ids);
	}

}
