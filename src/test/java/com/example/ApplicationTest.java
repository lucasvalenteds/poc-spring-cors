package com.example;

import com.example.internal.CrossOriginConfigurationProperties;
import com.example.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    private CrossOriginConfigurationProperties corsProperties;
    private WebClient webClient;

    @BeforeEach
    public void beforeEach(ApplicationContext applicationContext) {
        final var environment = applicationContext.getEnvironment();
        final var serverPort = environment.getRequiredProperty("local.server.port", Integer.class);

        this.corsProperties = applicationContext.getBean(CrossOriginConfigurationProperties.class);
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + serverPort)
                .build();
    }

    @Test
    void acceptingRequestFromOriginAllowed() {
        final var origin = "http://localhost:4200";

        final var responseBody = webClient.get()
                .uri("/")
                .header(HttpHeaders.ORIGIN, origin)
                .exchangeToMono(response -> response.bodyToMono(Message.class));

        StepVerifier.create(responseBody)
                .assertNext(message -> assertEquals("OK", message.message()))
                .verifyComplete();

        assertThat(corsProperties.getAllowedUrls()).contains(origin);
    }

    @Test
    void blockingRequestFromOriginNotAllowed() {
        final var origin = "http://localhost:5000";

        final var errorResponse = webClient.get()
                .uri("/")
                .header(HttpHeaders.ORIGIN, origin)
                .exchangeToMono(ClientResponse::createError);

        StepVerifier.create(errorResponse)
                .consumeErrorWith(throwable -> {
                    assertEquals(WebClientResponseException.Forbidden.class, throwable.getClass());

                    final var exception = (WebClientResponseException.Forbidden) throwable;
                    assertEquals("Invalid CORS request", exception.getResponseBodyAsString());
                })
                .verify();

        assertThat(corsProperties.getAllowedUrls()).doesNotContain(origin);
    }
}
