package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record TechnologyResponse(
        @Schema(description = "Unique identifier of the technology", example = "1")
        Long id,

        @Schema(description = "Name of the technology", example = "Reactive Spring")
        String name,

        @Schema(description = "Description of the technology", example = "A reactive framework for Spring applications")
        String description
) {
}
