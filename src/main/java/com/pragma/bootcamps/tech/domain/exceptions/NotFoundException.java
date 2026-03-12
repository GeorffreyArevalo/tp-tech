package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;

public class NotFoundException extends TechnologyException {

    public NotFoundException(String message) {
        super(BusinessCodesException.NOT_FOUND, 404, message);
    }

}
