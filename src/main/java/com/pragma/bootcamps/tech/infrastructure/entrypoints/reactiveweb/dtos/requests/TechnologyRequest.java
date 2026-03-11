package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TechnologyRequest(

        @NotBlank( message = "Name is required")
        @Size(max = 50, message = "Name must be less than 50 characters")
        @Schema(description = "Name of the technology", example = "Reactive Spring", maxLength = 50)
        String name,

        @NotBlank( message = "Description is required")
        @Size(max = 90, message = "Description must be less than 50 characters")
        @Schema(description = "Description of the technology", example = "A reactive framework for Spring applications", maxLength = 90)
        String description
) {
}
