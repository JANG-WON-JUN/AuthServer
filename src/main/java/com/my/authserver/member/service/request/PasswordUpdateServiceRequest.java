package com.my.authserver.member.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordUpdateServiceRequest {

	private Long id;
	private String password;
	private String newPassword;

	@Builder
	private PasswordUpdateServiceRequest(Long id, String password, String newPassword) {
		this.id = id;
		this.password = password;
		this.newPassword = newPassword;
	}

}
