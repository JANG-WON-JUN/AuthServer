package com.my.authserver.common.utils;

import static java.time.LocalDateTime.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import org.springframework.util.ObjectUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import jakarta.servlet.http.Cookie;

public class CommonUtils {

	public static LocalDateTime relativeMinuteFromNow(int minutes) {
		return now().plusMinutes(minutes);
	}

	public static LocalDateTime relativeDayFromNow(int days) {
		return now().plusDays(days);
	}

	public static LocalDateTime relativeMonthFromNow(int months) {
		return now().plusMonths(months);
	}

	public static boolean isEmpty(String str) {
		if (str == null || "null".equals(str)) {
			return true;
		}
		return ObjectUtils.isEmpty(str.trim());
	}

	public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
		try {
			return new BooleanBuilder(f.get());
		} catch (Exception e) {
			return new BooleanBuilder();
		}
	}

	public static boolean isValidDate(Integer year, Integer month, Integer day) {
		if (year == null || month == null || day == null) {
			return false;
		}

		try {
			LocalDate.of(year, month, day);
			return true;
		} catch (DateTimeException ex) {
			return false;
		}
	}

	public static Cookie findCookieByName(Cookie[] cookies, String name) {
		if (cookies == null || cookies.length == 0 || name == null) {
			return null;
		}

		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(name))
			.findFirst()
			.orElse(null);
	}

	public static String generateRandomCode(int digit) {
		StringBuilder code = new StringBuilder();
		RandomGenerator generator = RandomGenerator.of("L128X256MixRandom");

		for (int i = 0; i < digit; i++) {
			code.append(generator.nextInt(10));
		}

		return code.toString();
	}
}
