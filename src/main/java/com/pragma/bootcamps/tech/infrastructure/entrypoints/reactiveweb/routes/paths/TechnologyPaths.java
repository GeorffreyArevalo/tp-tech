package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.routes.paths;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "routes.paths")
public class TechnologyPaths {
    private String tech;
    private String associate;
    private String technologiesByCapabilityId;
    private String deleteTechnologiesByCapabilityIds;
}
