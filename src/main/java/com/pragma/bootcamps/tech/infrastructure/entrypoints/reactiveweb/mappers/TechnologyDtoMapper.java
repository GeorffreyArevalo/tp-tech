package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.mappers;

import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests.TechnologyRequest;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.responses.TechnologyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TechnologyDtoMapper {

    Technology toDomain(TechnologyRequest request);
    TechnologyResponse toResponse(Technology technology);

}
