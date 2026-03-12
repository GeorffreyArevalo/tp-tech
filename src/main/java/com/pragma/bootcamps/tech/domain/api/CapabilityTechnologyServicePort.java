package com.pragma.bootcamps.tech.domain.api;

import com.pragma.bootcamps.tech.domain.models.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityTechnologyServicePort {

    Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds);
    Flux<Technology> getTechnologiesByCapabilityId(Long capabilityId);

}
