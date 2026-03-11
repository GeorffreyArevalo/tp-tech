package com.pragma.bootcamps.tech.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCodesException {

    TECHNOLOGY_ALREADY_EXISTS("04-TECH");

    private final String code;

    BusinessCodesException(String code) {
        this.code = code;
    }

}
