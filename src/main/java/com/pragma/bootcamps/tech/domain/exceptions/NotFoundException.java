package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;

public class NotFoundException extends TechnologyException {

    public NotFoundException(String message) {
        super(BusinessHttpCodes.NOT_FOUND, 404, message);
    }

}
