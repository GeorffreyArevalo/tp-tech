package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.tech.domain.api.CapabilityTechnologyServicePort;
import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityTechnologyRequest;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.mappers.TechnologyDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.constants.CapabilityTechnologyHandlerLogMessages.*;
import static com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapabilityTechnologyHandler {

    private final CapabilityTechnologyServicePort capabilityTechnologyServicePort;
    private final TechnologyDtoMapper mapper;

    public Mono<ServerResponse> listenAssociateTechnologies(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapabilityTechnologyRequest.class)
                .doOnNext(request -> log.info(RECEIVED_ASSOCIATION_REQUEST, request))
                .flatMap(request ->
                        capabilityTechnologyServicePort.associateTechnologies(request.capabilityId(), request.technologyIds())
                )
                .then(ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(BusinessHttpCodes.SAVE_CAP_TECH.getCode(), null))
                );
    }

    public Mono<ServerResponse> getTechnologiesByCapabilityId(ServerRequest request) {
        Long capabilityId = Long.valueOf(request.pathVariable("capabilityId"));
        return capabilityTechnologyServicePort.getTechnologiesByCapabilityId(capabilityId)
                .map(mapper::toTechnologySummaryResponse)
                .collectList()
                .doOnNext(list -> log.info(TECHS_BY_CAPABILITY_RESPONSE, capabilityId, list))
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(BusinessHttpCodes.OK.getCode(), list)));
    }

    public Mono<ServerResponse> listenDeleteTechnologiesByCapabilityIds(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParams().get("ids"))
                .map(ids -> ids.stream().map(Long::valueOf).toList())
                .filter(ids -> !ids.isEmpty())
                .flatMap(capabilityIds ->
                        capabilityTechnologyServicePort.deleteTechnologiesByCapabilityIds(capabilityIds)
                                .then(ServerResponse.noContent().build())
                                .doOnSuccess(resp -> log.info(SUCCESS_CLEANUP, capabilityIds))
                )
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(e -> log.error(ERROR_CLEANUP, e.getMessage()));
    }

}
