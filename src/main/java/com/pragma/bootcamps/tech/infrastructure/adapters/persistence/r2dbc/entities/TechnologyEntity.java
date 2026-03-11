package com.pragma.bootcamps.tech.infrastructure.adapters.persistence.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("technology")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class TechnologyEntity {

    @Id
    private Long id;
    private String name;
    private String description;

}
