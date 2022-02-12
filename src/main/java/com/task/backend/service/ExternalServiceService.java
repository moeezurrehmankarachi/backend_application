package com.task.backend.service;

import com.task.backend.domainobject.ExternalService;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExternalServiceService {
    Flux<ExternalService>  findAll();
    Mono<ExternalService> add(ExternalService externalService);
    Mono<ExternalService> update(ExternalService externalService);
    Mono<Integer> remove(Long id) ;
    Mono<ResponseEntity<String>> sendGetRequest(String url);
    Mono<ExternalService> processResponse(ExternalService externalService, ResponseEntity response);
}
