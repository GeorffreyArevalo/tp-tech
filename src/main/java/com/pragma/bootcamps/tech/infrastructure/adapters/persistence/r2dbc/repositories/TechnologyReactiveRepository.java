package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TechnologyReactiveRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<TechnologyEntity> findByNameIgnoreCase(String name);
}
