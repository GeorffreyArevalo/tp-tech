package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;

public class TechnologyAlreadyExistsException extends TechnologyException{

    public TechnologyAlreadyExistsException(String message) {
        super(BusinessHttpCodes.TECHNOLOGY_ALREADY_EXISTS, 400, message);
    }

}
