package com.example.apodui.services;


import com.example.apodui.dto.ApodData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApodDataRetriever {
    @Value("${imagestore.url}")
    private String pictureServiceUrl;

    private final WebClient client;

    public Mono<ApodData> getApodData(LocalDate date){
        return client.get()
                .uri(pictureServiceUrl + "?date=" + date.toString())
                .retrieve()
                .bodyToMono(ApodData.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, badRequest -> Mono.empty());
    }
}
