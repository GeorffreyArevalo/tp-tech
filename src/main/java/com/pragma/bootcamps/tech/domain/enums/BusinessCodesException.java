package com.pragma.bootcamps.tech.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCodesException {

    TECHNOLOGY_ALREADY_EXISTS("04-TECH"),
    INVALID_FIELDS("04-FIELDS"),
    INTERNAL_SERVER_ERROR("05-ERROR"),
    SAVE_TECH("02-TECH");

    private final String code;

    BusinessCodesException(String code) {
        this.code = code;
    }

}
