package com.pragma.bootcamps.tech.domain.usecases;

import com.pragma.bootcamps.tech.domain.api.TechnologyServicePort;
import com.pragma.bootcamps.tech.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyUseCase implements TechnologyServicePort {

    private final TechnologyPersistencePort technologyPersistencePort;

    @Override
    public Mono<Technology> save(Technology technology) {
        return this.technologyPersistencePort.findByName(technology.getName())
                .flatMap(existingTechnology ->
                        Mono.<Technology>error(new TechnologyAlreadyExistsException(String.format("Technology with name %s already exists.", existingTechnology.getName())
                )))
                .switchIfEmpty( Mono.defer(() -> this.technologyPersistencePort.save(technology)) );
    }
}
