package com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;
import com.pragma.bootcamps.tech.domain.exceptions.TechnologyException;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import com.pragma.bootcamps.tech.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final String STATUS_KEY = "status";
    private static final String BODY_KEY = "body";
    private static final String MSG_INVALID_FIELDS = "Request invalid fields";
    private static final String MSG_INTERNAL_ERROR = "Internal Server Error";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        BusinessResponse<?> responseBody = switch (error) {
            case TechnologyException be -> HandlersResponseUtil.buildBodyFailureResponse(
                    be.getCode().getCode(),
                    be.getMessage(),
                    null
            );

            case ConstraintViolationException cve -> HandlersResponseUtil.buildBodyFailureResponse(
                    BusinessCodesException.INVALID_FIELDS.getCode(),
                    MSG_INVALID_FIELDS,
                    formatConstraintViolations(cve)
            );

            default -> HandlersResponseUtil.buildBodyFailureResponse(
                    BusinessCodesException.INTERNAL_SERVER_ERROR.getCode(),
                    MSG_INTERNAL_ERROR,
                    null
            );
        };

        errorAttributes.put(STATUS_KEY, determineHttpStatus(error));
        errorAttributes.put(BODY_KEY, responseBody);
        return errorAttributes;
    }

    private List<String> formatConstraintViolations(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
    }

    private int determineHttpStatus(Throwable error) {
        return switch (error) {
            case TechnologyException be -> be.getStatusCode();
            case ConstraintViolationException ignored -> HttpStatus.BAD_REQUEST.value();
            default -> HttpStatus.INTERNAL_SERVER_ERROR.value();
        };
    }

}
