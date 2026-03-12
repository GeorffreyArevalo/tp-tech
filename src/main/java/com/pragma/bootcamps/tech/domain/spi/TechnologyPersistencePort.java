package com.pragma.bootcamps.tech.domain.spi;

import com.pragma.bootcamps.tech.domain.models.Technology;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyPersistencePort {

    Mono<Technology> save(Technology technology);
    Mono<Technology> findByName(String name);
    Mono<Long> countByIds(List<Long> technologyIds);

}
