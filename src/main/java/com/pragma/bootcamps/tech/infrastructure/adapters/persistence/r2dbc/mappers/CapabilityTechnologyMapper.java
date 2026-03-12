package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.mappers;

import com.pragma.bootcamps.tech.domain.models.CapabilityTechnology;
import com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities.CapabilityTechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CapabilityTechnologyMapper {

    CapabilityTechnology toDomain(CapabilityTechnologyEntity entity);
    CapabilityTechnologyEntity toEntity(CapabilityTechnology capabilityTechnology);

}
