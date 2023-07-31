package com.my.authserver.common.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class Result<T> {

	private T data;

	@Builder
	private Result(T data) {
		this.data = data;
	}

	public static <T> Result<T> of(T data) {
		return new Result(data);
	}
}
