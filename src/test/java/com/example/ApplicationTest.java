package com.example;

import com.example.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    private WebClient webClient;

    @BeforeEach
    public void beforeEach(ApplicationContext applicationContext) {
        final var environment = applicationContext.getEnvironment();
        final var serverPort = environment.getRequiredProperty("local.server.port", Integer.class);

        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + serverPort)
                .build();
    }

    @Test
    void acceptingRequestFromOriginA() {
        final var responseBody = webClient.get()
                .uri("/")
                .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                .exchangeToMono(response -> response.bodyToMono(Message.class));

        StepVerifier.create(responseBody)
                .assertNext(message -> assertEquals("OK", message.message()))
                .verifyComplete();
    }

    @Test
    void acceptingRequestFromOriginB() {
        final var responseBody = webClient.get()
                .uri("/")
                .header(HttpHeaders.ORIGIN, "http://localhost:5000")
                .exchangeToMono(response -> response.bodyToMono(Message.class));

        StepVerifier.create(responseBody)
                .assertNext(message -> assertEquals("OK", message.message()))
                .verifyComplete();
    }
}
