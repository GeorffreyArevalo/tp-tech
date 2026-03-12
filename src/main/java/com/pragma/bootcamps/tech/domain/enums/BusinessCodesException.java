package com.pragma.bootcamps.tech.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCodesException {

    TECHNOLOGY_ALREADY_EXISTS("04-TECH"),
    INVALID_FIELDS("04-FIELDS"),
    INTERNAL_SERVER_ERROR("05-ERROR"),
    SAVE_TECH("02-TECH"),
    CONFLICT("04-CF"),
    NOT_FOUND("04-NF");

    private final String code;

    BusinessCodesException(String code) {
        this.code = code;
    }

}
