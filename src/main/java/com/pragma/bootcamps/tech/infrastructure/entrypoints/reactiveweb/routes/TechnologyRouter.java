package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.routes;

import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.handlers.TechnologyHandler;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.routes.paths.TechnologyPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class TechnologyRouter {

    private final TechnologyPaths technologyPaths;

    public RouterFunction<ServerResponse> routerFunction(TechnologyHandler handler) {
        return route()
                .POST(technologyPaths.getTech(), handler::listenSaveTech)
                .build();
    }

}
