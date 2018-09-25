package org.nfa.atropos;

import org.junit.Test;
import org.nfa.atropos.model.Article;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

public class TestWebTestClient {
	
	private final WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
	 
    @Test
    public void testCreateArticle() throws Exception {
        final Article article = new Article();
        article.setName("Test");
        client.post().uri("/Article")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("name").isEqualTo("Test");
    }

}
