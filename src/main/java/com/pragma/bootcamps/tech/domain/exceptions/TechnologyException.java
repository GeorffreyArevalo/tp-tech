package com.pragma.bootcamps.tech.domain.exceptions;

import com.pragma.bootcamps.tech.domain.enums.BusinessCodesException;
import lombok.Getter;

@Getter
public class TechnologyException extends RuntimeException{

    private final BusinessCodesException code;
    private final int statusCode;

    public TechnologyException(BusinessCodesException code, int statusCode, String message) {
        super(message);
        this.code = code;
        this.statusCode = statusCode;
    }

}
