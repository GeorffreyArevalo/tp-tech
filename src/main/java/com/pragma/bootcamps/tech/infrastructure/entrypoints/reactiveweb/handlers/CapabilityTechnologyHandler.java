package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.tech.domain.api.CapabilityTechnologyServicePort;
import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityTechnologyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapabilityTechnologyHandler {

    private final CapabilityTechnologyServicePort capabilityTechnologyServicePort;

    public Mono<ServerResponse> listenAssociateTechnologies(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapabilityTechnologyRequest.class)
                .doOnNext(request -> log.info("Received capability-technology association request: {}", request))
                .flatMap(request ->
                        capabilityTechnologyServicePort.associateTechnologies(request.capabilityId(), request.technologyIds())
                )
                .then(ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(BusinessHttpCodes.SAVE_CAP_TECH.getCode(), null))
                );
    }


}
