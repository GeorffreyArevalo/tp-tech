package com.pragma.bootcamps.tech.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyServicePort {

    Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds);

}
