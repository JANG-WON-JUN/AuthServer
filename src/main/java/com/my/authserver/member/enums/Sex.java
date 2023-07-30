package com.my.authserver.member.enums;

import static com.my.authserver.common.web.converter.enumconverter.EnumConverterService.*;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Sex {
	MALE, FEMALE;

	@JsonCreator
	public static Sex parse(String source) {
		return stringToSex(source);
	}
}
