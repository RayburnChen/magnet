package org.nfa.atropos;

import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nfa.atropos.model.Article;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAtroposApplication.class)
public class TestWebClient {

	@Test
	public void testHttpClient() {
		final Article article = new Article();
        article.setName("Test");
        final WebClient client = WebClient.create("http://localhost:8080/article");
        final Mono<Article> createdArticle = client.post()
                .uri("")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(article), Article.class)
                .exchange()
                .flatMap(response -> response.bodyToMono(Article.class));
        System.out.println(createdArticle.block());
	}
	
	@Test
	public void testSeverSentEvent() {
		final WebClient client = WebClient.create();
        client.get()
                .uri("http://localhost:8080/sse/randomNumbers")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(response -> response.body(BodyExtractors.toFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })))
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)
                .buffer(10)
                .doOnNext(System.out::println)
                .blockFirst();
	}

}
