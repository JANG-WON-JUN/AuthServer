package com.my.authserver.member.web.response;

import com.my.authserver.domain.entity.member.Password;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResponse {

	private Long id;

	@Builder
	private PasswordResponse(Long id) {
		this.id = id;
	}

	public static PasswordResponse from(Password password) {
		return PasswordResponse.builder()
			.id(password.getId())
			.build();
	}

}
