package com.my.authserver.common.utils;

import static java.util.Locale.*;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageSourceUtils {

	private final MessageSource messageSource;

	public String getMessage(String code) {
		return messageSource.getMessage(code, null, getDefault());
	}

	public String getMessage(String code, @Nullable Object[] args) {
		return messageSource.getMessage(code, args, getDefault());
	}

	public String getMessage(String code, @Nullable Object[] args, Locale locale) {
		return messageSource.getMessage(code, args, locale);
	}
}
