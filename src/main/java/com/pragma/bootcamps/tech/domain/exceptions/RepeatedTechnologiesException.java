package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;

public class RepeatedTechnologiesException extends TechnologyException {
    public RepeatedTechnologiesException(String message) {
        super(BusinessCodesException.CONFLICT, 409, message);
    }
}
