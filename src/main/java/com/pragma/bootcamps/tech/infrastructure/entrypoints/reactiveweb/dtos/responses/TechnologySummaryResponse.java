package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechnologySummaryResponse {
    private Long id;
    private String name;
}
