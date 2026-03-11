package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.handlers;


import com.pragma.bootcamps.tech.domain.api.TechnologyServicePort;
import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests.TechnologyRequest;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.mappers.TechnologyDtoMapper;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TechnologyHandler {

    private final TechnologyServicePort technologyServicePort;
    private final TechnologyDtoMapper technologyDtoMapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveTech(ServerRequest request) {
        return  request.bodyToMono(TechnologyRequest.class)
                .doOnNext( techRequest -> log.info("Saving {} technology", techRequest.name()) )
                .flatMap( validatorUtil::validate )
                .map( technologyDtoMapper::toDomain )
                .flatMap( technologyServicePort::save )
                .map( technologyDtoMapper::toResponse )
                .flatMap( savedTech ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(BusinessCodesException.SAVE_TECH.getCode(), savedTech))
                );
    }





}
