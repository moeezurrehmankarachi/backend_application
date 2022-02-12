package com.task.backend.dataaccessobject;

import com.task.backend.domainobject.ExternalService;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExternalServicesRepository extends ReactiveCrudRepository<ExternalService, Integer> {
    Mono<Integer> deleteById(Long id);
}
