package com.pragma.bootcamps.tech.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyPersistencePort {
    Mono<Void> saveAll(Long capabilityId, List<Long> technologyIds);
}
