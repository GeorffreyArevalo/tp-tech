package com.pragma.bootcamps.tech.domain.spi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyPersistencePort {
    Mono<Void> saveAll(Long capabilityId, List<Long> technologyIds);
    Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId);
    Flux<Long> findTechnologyIdsByCapabilityIds(List<Long> capabilityIds);
    Mono<Long> countOtherCapacityAssociations(Long technologyId, List<Long> capabilityIds);
    Mono<Void> deleteAssociationsByCapabilityIds(List<Long> capabilityIds);
}
