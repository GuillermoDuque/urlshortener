package com.dio.urlshortener.integration;

import com.dio.urlshortener.presentation.dto.ShortenUrlResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class ShortUrlIntegrationTest {

    @Autowired
    MockMvcTester tester;

    @Test
    void shouldCreateShortUrl() {
        String bodyJson = """
                {
                  "longUrl": "https://example.com"
                }
                """;

        var testResult = tester.post()
                .uri("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson).exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .isLenientlyEqualTo(bodyJson);


    }

    @Test
    void shouldResolveShortUrlRedirect() {
        String longUrlJson = """
            {
              "longUrl": "https://example.com"
            }
            """;

        var creationResult = tester.post()
                .uri("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(longUrlJson)
                .exchange();

        assertThat(creationResult)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(ShortenUrlResponse.class)
                .satisfies(
                        shortenUrlResponse -> {
                            var redirectResult = tester.get()
                                    .uri("/" + shortenUrlResponse.shortCode())
                                    .exchange();
                            assertThat(redirectResult)
                                    .hasStatus(HttpStatus.FOUND)
                                    .hasHeader("Location", "https://example.com");
                        }
                );

    }

    @Test
    void shouldReturnNotFoundForNonExistentShortUrlCode() {
        var testResult = tester.get()
                .uri("/nonexistentCode")
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequestForInvalidUrlFormat() {
        String bodyJson = """
            {
              "longUrl": "invalid-url"
            }
            """;

        var testResult = tester.post()
                .uri("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson)
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.BAD_REQUEST);
    }




}

