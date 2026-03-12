package com.pragma.bootcamps.tech.domain.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapabilityTechnology {
    private Long id;
    private Long capabilityId;
    private Long technologyId;
}
