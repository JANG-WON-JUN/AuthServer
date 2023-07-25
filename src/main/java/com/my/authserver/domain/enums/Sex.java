package com.my.authserver.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import static com.my.authserver.web.converter.enumconverter.EnumConverterService.stringToSex;


public enum Sex {
    MALE, FEMALE;

    @JsonCreator
    public static Sex parse(String source) {
        return stringToSex(source);
    }
}
