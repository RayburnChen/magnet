package org.nfa.atropos;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.junit.Test;
import org.nfa.atropos.model.Article;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MagnetAtroposApplication.class)
public class TestWebClient {

	@Test
	public void testHttpClient() {
		final Article article = new Article();
		article.setId(UUID.randomUUID().toString());
        article.setName("Test");
        article.setEnabled(true);
        final WebClient client = WebClient.create("http://localhost:8120");
        final Mono<Article> createdArticle = client.post()
                .uri("/article")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(article), Article.class)
                .exchange()
                .flatMap(response -> response.bodyToMono(Article.class));
        System.out.println(createdArticle.block());
	}
	
	@Test
	public void testSeverSentEvent() {
		final WebClient client = WebClient.create();
        Flux<List<String>> reponse = client.get()
                .uri("http://localhost:8120/article/random/number")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(response -> response.body(BodyExtractors.toFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })))
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)
                .buffer(10)
                .doOnNext(System.out::println);
        System.out.println(reponse.blockFirst());
	}

}
