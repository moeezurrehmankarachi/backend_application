package com.task.backend.service;

import com.task.backend.dataaccessobject.ExternalServicesRepository;
import com.task.backend.domainobject.ExternalService;
import com.task.backend.exceptions.EntityNotFoundException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ExternalServiceServiceImplementation implements ExternalServiceService {
    private Logger logger = LoggerFactory.getLogger(ExternalServiceServiceImplementation.class);
    private final ExternalServicesRepository externalServicesRepository;

    @Value("${request.timeout}")
    private Long timeout;

    public ExternalServiceServiceImplementation(ExternalServicesRepository externalServicesRepository) {
        this.externalServicesRepository = externalServicesRepository;
    }

    @Override
    public Flux<ExternalService> findAll() {
        return externalServicesRepository.findAll();
    }

    @Override
    public Mono<Integer> remove(Long id) {
        return this.externalServicesRepository.deleteById(id)
                .flatMap(result ->
                        result.equals(1)
                                ? Mono.just(result)
                                : Mono.error(new EntityNotFoundException("External Service with id: " + id + " not found!!"))
                ).log("External Service with id: " + id + " deleted");
    }

    @Transactional
    public Mono<ExternalService> save(ExternalService externalService) {
        return this.externalServicesRepository.save(externalService);
    }

    @Override
    public Mono<ResponseEntity<String>> sendGetRequest(String url) {
            logger.info("Sending Request to url: " + url);
            return WebClient
                    .create() //Default Settings
                    .get()
                    .uri(url)
                    .retrieve()
                    .toEntity(String.class)
                    .timeout(Duration.ofSeconds(timeout))
                    .onErrorMap(ReadTimeoutException.class, ex -> new HttpTimeoutException("ReadTimeout"))
                    .doOnError(WriteTimeoutException.class, ex -> logger.error("WriteTimeout"))
                    .doOnError(Exception.class, ex -> logger.error(ex.getMessage()))
                    .onErrorResume(e -> Mono.just("Error " + e.getMessage())
                            .map(s -> ResponseEntity.internalServerError().body(s)));
    }

    @Override
    public Mono<ExternalService> processResponse(ExternalService externalService, ResponseEntity response) {
        logger.info("Response Received for: " + externalService + " , Status Recieved: " + response.getStatusCode());
        if (response.getStatusCode().is2xxSuccessful()) {
            externalService.setStatus("1");
        } else {
            externalService.setStatus("0");
        }
        externalService.setLast_verified_datetime(LocalDateTime.now());
        return save(externalService);
    }

    @Override
    public Mono<ExternalService> add(ExternalService externalService) {
        sendGetRequest(externalService.getUrl())
                .flatMap(stringResponseEntity -> processResponse(externalService, stringResponseEntity))
                .subscribe(externalService1 -> logger.info("External Service saved: " + externalService));
        return Mono.just(externalService);
    }

    @Override
    public Mono<ExternalService> update(ExternalService externalService) {
        sendGetRequest(externalService.getUrl())
                .flatMap(stringResponseEntity ->  processResponse(externalService, stringResponseEntity))
                .onErrorResume(e -> Mono.error(new EntityNotFoundException("Error while updating : " + externalService + ", error: " + e.getMessage())))
                .subscribe(externalService1 -> logger.info("External Service saved: " + externalService));
        return Mono.just(externalService);
    }
}
