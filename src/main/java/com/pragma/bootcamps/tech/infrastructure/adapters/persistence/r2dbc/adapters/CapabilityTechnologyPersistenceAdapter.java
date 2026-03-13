package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.tech.domain.spi.CapabilityTechnologyPersistencePort;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.CapabilityTechnologyEntity;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories.CapabilityTechnologyReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.constants.CapabilityTechnologyLogMessages.*;

@Slf4j
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
                .doOnComplete(() -> log.info(SAVE_ALL_ASSOCIATIONS_COMPLETED, capabilityId))
                .then()
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId) {
        return capabilityTechnologyReactiveRepository.findAllByCapabilityId(capabilityId)
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .doOnNext(id -> log.info(FIND_TECHNOLOGY_IDS_BY_CAPABILITY_ID, capabilityId, id));
    }

    @Override
    public Flux<Long> findTechnologyIdsByCapabilityIds(List<Long> capabilityIds) {
        return capabilityTechnologyReactiveRepository.findAllByCapabilityIdIn(capabilityIds)
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .doOnNext(id -> log.info(FIND_TECHNOLOGY_IDS_BY_CAPABILITY_IDS, id));
    }

    @Override
    public Mono<Long> countOtherCapacityAssociations(Long technologyId, List<Long> capabilityIds) {
        return capabilityTechnologyReactiveRepository.countByTechnologyIdAndCapabilityIdNotIn(technologyId, capabilityIds)
                .doOnNext(count -> log.info(COUNT_OTHER_CAPACITY_ASSOCIATIONS, technologyId, capabilityIds, count));
    }

    @Override
    public Mono<Void> deleteAssociationsByCapabilityIds(List<Long> capabilityIds) {
        return capabilityTechnologyReactiveRepository.deleteAllByCapabilityIdIn(capabilityIds)
                .doOnSuccess(unused -> log.info(DELETE_ASSOCIATIONS_BY_CAPABILITY_IDS, capabilityIds))
                .as(transactionalOperator::transactional);
    }
}
