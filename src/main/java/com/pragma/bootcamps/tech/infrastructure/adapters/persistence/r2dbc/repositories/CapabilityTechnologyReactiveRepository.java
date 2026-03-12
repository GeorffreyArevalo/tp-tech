package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.CapabilityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyReactiveRepository extends ReactiveCrudRepository<CapabilityTechnologyEntity, Long> {

    Mono<Long> countAllByTechnologyIdIn(List<Long> technologyIds);

}
