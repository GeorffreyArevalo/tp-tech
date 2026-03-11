package com.pragma.bootcamps.tech.domain.api;

import com.pragma.bootcamps.tech.domain.models.Technology;
import reactor.core.publisher.Mono;

public interface TechnologyServicePort {

    Mono<Technology> save(Technology technology);

}
