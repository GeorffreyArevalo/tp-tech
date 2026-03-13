package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.adapters;

import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.mappers.TechnologyEntityMapper;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.repositories.TechnologyReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.constants.TechnologyLogMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyPersistencePort {

    private final TechnologyReactiveRepository technologyReactiveRepository;
    private final TechnologyEntityMapper mapper;

    @Override
    public Mono<Technology> save(Technology technology) {
        return this.technologyReactiveRepository.save(
                mapper.toEntity(technology)
        ).map( mapper::toDomain )
                .doOnNext(saved -> log.info(SAVE_TECHNOLOGY, saved.getId(), saved.getName()));
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return this.technologyReactiveRepository.findByNameIgnoreCase(name)
                .map( mapper::toDomain )
                .doOnNext(found -> log.info(FIND_TECHNOLOGY_BY_NAME, found.getId(), found.getName()));
    }

    @Override
    public Mono<Long> countByIds(List<Long> technologyIds) {
        return technologyReactiveRepository.countByIdIn(technologyIds)
                .doOnNext(count -> log.info(COUNT_BY_IDS, technologyIds, count));
    }

    @Override
    public Mono<Technology> findTechnologyById(Long technologyId) {
        return technologyReactiveRepository.findById(technologyId)
                .map( mapper::toDomain )
                .doOnNext(tech -> log.info(FIND_TECHNOLOGY_BY_ID, technologyId, tech.getId(), tech.getName()));
    }

    @Override
    public Mono<Void> deleteTechnologiesByIds(List<Long> technologyIds) {
        return technologyReactiveRepository.deleteAllById(technologyIds)
                .doOnSuccess(unused -> log.info(DELETE_TECHNOLOGIES_BY_IDS, technologyIds));
    }
}
