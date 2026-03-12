package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;

public class RepeatedTechnologiesException extends TechnologyException {
    public RepeatedTechnologiesException(String message) {
        super(BusinessHttpCodes.CONFLICT, 409, message);
    }
}
