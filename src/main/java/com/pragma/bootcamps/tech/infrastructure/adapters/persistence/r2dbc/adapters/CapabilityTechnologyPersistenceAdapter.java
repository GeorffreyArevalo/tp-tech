package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.tech.domain.spi.CapabilityTechnologyPersistencePort;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.CapabilityTechnologyEntity;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories.CapabilityTechnologyReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapabilityTechnologyPersistenceAdapter implements CapabilityTechnologyPersistencePort {

    private final CapabilityTechnologyReactiveRepository capabilityTechnologyReactiveRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Void> saveAll(Long capabilityId, List<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .map(techId -> CapabilityTechnologyEntity.builder()
                        .capabilityId(capabilityId)
                        .technologyId(techId)
                        .build())
                .collectList()
                .flatMapMany(capabilityTechnologyReactiveRepository::saveAll)
                .then()
                .as(transactionalOperator::transactional);
    }
}
