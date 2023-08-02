package com.my.authserver.member.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordUpdateServiceRequest {

	private Long id;
	private String password;
	private String passwordConfirm;

	@Builder
	private PasswordUpdateServiceRequest(Long id, String password, String passwordConfirm) {
		// todo 나중에 컨트롤러쪽 updateRequest에서 toServiceRequest로 변환됨
		this.id = id;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}

}
