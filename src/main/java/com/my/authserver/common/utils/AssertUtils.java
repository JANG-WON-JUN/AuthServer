package com.my.authserver.common.utils;

import java.util.Collection;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.my.authserver.common.web.exception.AuthServerException;

public class AssertUtils {

	public static void state(boolean expression, AuthServerException exception) {
		if (!expression) {
			throw exception;
		}
	}

	public static void isNull(@Nullable Object object, AuthServerException exception) {
		if (object != null) {
			throw exception;
		}
	}

	public static void notNull(@Nullable Object object, AuthServerException exception) {
		if (object == null) {
			throw exception;
		}
	}

	public static void hasLength(@Nullable String text, AuthServerException exception) {
		if (!StringUtils.hasLength(text)) {
			throw exception;
		}
	}

	public static void hasText(@Nullable String text, AuthServerException exception) {
		if (!StringUtils.hasText(text)) {
			throw exception;
		}
	}

	public static void doesNotContain(@Nullable String textToSearch, String substring, AuthServerException exception) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
			textToSearch.contains(substring)) {
			throw exception;
		}
	}

	public static void notEmpty(@Nullable Object[] array, AuthServerException exception) {
		if (ObjectUtils.isEmpty(array)) {
			throw exception;
		}
	}

	public static void noNullElements(@Nullable Object[] array, AuthServerException exception) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw exception;
				}
			}
		}
	}

	public static void notEmpty(@Nullable Collection<?> collection, AuthServerException exception) {
		if (CollectionUtils.isEmpty(collection)) {
			throw exception;
		}
	}

	public static void noNullElements(@Nullable Collection<?> collection, AuthServerException exception) {
		if (collection != null) {
			for (Object element : collection) {
				if (element == null) {
					throw exception;
				}
			}
		}
	}

	public static void notEmpty(@Nullable Map<?, ?> map, AuthServerException exception) {
		if (CollectionUtils.isEmpty(map)) {
			throw exception;
		}
	}

	public static void isInstanceOf(Class<?> type, @Nullable Object obj, AuthServerException exception) {
		notNull(type, exception);
		if (!type.isInstance(obj)) {
			throw exception;
		}
	}

	public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, AuthServerException exception) {
		notNull(superType, exception);
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw exception;
		}
	}

}