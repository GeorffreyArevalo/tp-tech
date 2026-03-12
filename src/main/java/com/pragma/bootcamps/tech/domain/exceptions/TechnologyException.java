package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessHttpCodes;
import lombok.Getter;

@Getter
public class TechnologyException extends RuntimeException{

    private final BusinessHttpCodes code;
    private final int statusCode;

    public TechnologyException(BusinessHttpCodes code, int statusCode, String message) {
        super(message);
        this.code = code;
        this.statusCode = statusCode;
    }

}
