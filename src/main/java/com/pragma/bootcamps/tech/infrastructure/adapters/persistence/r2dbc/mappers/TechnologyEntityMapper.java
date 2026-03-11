package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.mappers;

import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.TechnologyEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface TechnologyEntityMapper {

    Technology toDomain(TechnologyEntity entity);
    TechnologyEntity toEntity(Technology technology);

}
