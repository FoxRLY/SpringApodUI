package com.example.apodui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ApodDataRetrieverTest {

    static ObjectMapper jsonMapper = new ObjectMapper();
    static MockWebServer mockServer;
    static ApodDataRetriver imageDownloader;

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void init(){
        String host = String.format("http://localhost:%s", mockServer.getPort());
        WebClient client = WebClient.create();
        imageDownloader = new ApodDataRetriver(host, client);
    }

    @Test
    @DisplayName("Получение данных из APOD-сервиса по валидным данным")
    void downloadImageByDate() throws RuntimeException, JsonProcessingException {
        ApodData expectedData = new ApodData(
                "2020-02-02",
                "sdfkljwefl",
                "fdslkfj",
                "dslkfjsdf",
                "dsflkj");
        mockServer.enqueue(new MockResponse()
                .setBody(jsonMapper.writeValueAsString(expectedData))
                .addHeader("Content-Type", "application/json"));
        Optional<ApodData> returnedData = imageDownloader.getApodData(LocalDate.parse("2020-02-02")).blockOptional();
        returnedData.ifPresentOrElse(
                retreivedData -> {
                    assertEquals(retreivedData.date(), expectedData.date());
                    assertEquals(retreivedData.explanation(), expectedData.explanation());
                    assertEquals(retreivedData.hdurl(), expectedData.hdurl());
                    assertEquals(retreivedData.url(), expectedData.url());
                    assertEquals(retreivedData.title(), expectedData.title());
                },
                () -> fail("ApodData cannot be empty")
        );
    }


    @Test
    @DisplayName("Получение ошибки из APOD-сервиса по невалидным данным")
    void downloadImageByDateBad() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .addHeader("Content-Type", "application/json"));
        Optional<ApodData> returnedData = imageDownloader.getApodData(LocalDate.parse("2020-02-02")).blockOptional();
        assertEquals(Optional.empty(), returnedData);
    }
}