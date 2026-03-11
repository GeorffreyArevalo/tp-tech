package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.mappers.TechnologyEntityMapper;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories.TechnologyReactiveRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyPersistencePort {

    private final TechnologyReactiveRepository technologyReactiveRepository;
    private final TechnologyEntityMapper mapper;

    @Override
    public Mono<Technology> save(Technology technology) {
        return this.technologyReactiveRepository.save(
                mapper.toEntity(technology)
        ).map( mapper::toDomain );
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return this.technologyReactiveRepository.findByNameIgnoreCase(name)
                .map( mapper::toDomain );
    }
}
