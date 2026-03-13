package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.CapabilityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyReactiveRepository extends ReactiveCrudRepository<CapabilityTechnologyEntity, Long> {

    Flux<CapabilityTechnologyEntity> findAllByCapabilityId(Long capabilityId);
    Flux<CapabilityTechnologyEntity> findAllByCapabilityIdIn(List<Long> capabilityIds);
    Mono<Long> countByTechnologyId(Long technologyId);
    Mono<Long> countByTechnologyIdAndCapabilityIdNotIn(Long technologyId, List<Long> capabilityIds);
    Mono<Void> deleteAllByCapabilityIdIn(List<Long> capabilityIds);

}
