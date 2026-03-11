package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;

public class TechnologyAlreadyExistsException extends TechnologyException{

    public TechnologyAlreadyExistsException(String message) {
        super(BusinessCodesException.TECHNOLOGY_ALREADY_EXISTS, 400, message);
    }

}
