package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;

public class InvalidCountException extends TechnologyException {

    public InvalidCountException(String message) {
        super(BusinessHttpCodes.INVALID_FIELDS, 400, message);
    }

}
