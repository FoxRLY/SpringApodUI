package com.example.apodui;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApodDataRetriver {
    @Value("${imagestore.url}")
    private String pictureServiceUrl;

    private final WebClient client;

    @Autowired
    public ApodDataRetriver(@Value("${imagestore.url}") String url, WebClient client){
        this.pictureServiceUrl = url;
        this.client = client;
    }

    public Mono<ApodData> getApodData(LocalDate date){
        return client.get()
                .uri(pictureServiceUrl + "?date=" + date.toString())
                .retrieve()
                .bodyToMono(ApodData.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, badRequest -> Mono.empty());
    }
}
