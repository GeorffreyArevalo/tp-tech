package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.handlers;


import com.pragma.bootcamps.tech.domain.api.TechnologyServicePort;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.mappers.TechnologyDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TechnologyHandler {

    private final TechnologyServicePort technologyServicePort;
    private final TechnologyDtoMapper technologyDtoMapper;

}
