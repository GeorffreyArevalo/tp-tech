package com.pragma.bootcamps.tech.domain.usecases;

import com.pragma.bootcamps.tech.domain.api.CapabilityTechnologyServicePort;
import com.pragma.bootcamps.tech.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.tech.domain.exceptions.InvalidCountException;
import com.pragma.bootcamps.tech.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.tech.domain.exceptions.RepeatedTechnologiesException;
import com.pragma.bootcamps.tech.domain.spi.CapabilityTechnologyPersistencePort;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.tech.domain.constants.CapabilityConstants.MAX_TECHS;
import static com.pragma.bootcamps.tech.domain.constants.CapabilityConstants.MIN_TECHS;


@RequiredArgsConstructor
public class CapabilityTechnologyUseCase implements CapabilityTechnologyServicePort {

    private final CapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort;
    private final TechnologyPersistencePort technologyPersistencePort;

    @Override
    public Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds) {
        return Mono.just(technologyIds)
                .filter(ids -> isValidTechnologiesCount(ids, MIN_TECHS, MAX_TECHS))
                .switchIfEmpty(Mono.error(new InvalidCountException(ExceptionMessages.INVALID_COUNT.format(MIN_TECHS, MAX_TECHS))))
                .filter(this::hasNoRepeatedTechnologies)
                .switchIfEmpty(Mono.error(new RepeatedTechnologiesException(ExceptionMessages.REPEATED_TECHS.format())))
                .flatMap(ids -> technologyPersistencePort.countByIds(ids)
                        .filter(count -> count == ids.size())
                        .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.TECH_NOT_FOUND.format())))
                        .thenReturn(ids))
                .flatMap(ids -> capabilityTechnologyPersistencePort.saveAll(capabilityId, ids))
                .then();
    }

    public boolean isValidTechnologiesCount(List<Long> techIds, int min, int max) {
        return techIds != null && techIds.size() >= min && techIds.size() <= max;
    }

    public boolean hasNoRepeatedTechnologies(List<Long> techIds) {
        return techIds != null && techIds.stream().distinct().count() == techIds.size();
    }
}
