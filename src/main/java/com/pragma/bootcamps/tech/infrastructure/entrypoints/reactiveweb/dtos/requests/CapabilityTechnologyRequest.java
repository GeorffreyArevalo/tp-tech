package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests;

import java.util.List;

public record CapabilityTechnologyRequest(
        Long capabilityId,
        List<Long>technologyIds
) {
}
