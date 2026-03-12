package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;

public class InvalidCountException extends TechnologyException {

    public InvalidCountException(String message) {
        super(BusinessCodesException.INVALID_FIELDS, 400, message);
    }

}
