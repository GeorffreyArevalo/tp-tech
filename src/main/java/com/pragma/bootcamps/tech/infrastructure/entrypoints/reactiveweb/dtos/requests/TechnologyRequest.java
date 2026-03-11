package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TechnologyRequest(

        @NotBlank( message = "Name is required")
        @Size(max = 50, message = "Name must be less than 50 characters")
        String name,

        @NotBlank( message = "Description is required")
        @Size(max = 90, message = "Description must be less than 50 characters")
        String description
) {
}
