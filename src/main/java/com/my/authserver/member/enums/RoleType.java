package com.my.authserver.member.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
	// Spring Security에서는 권한을 나타내는 문자열의 접두어에 기본으로 ROLE_를 붙여주므로
	// 동일하게 Roles enum을 작성함
	ROLE_ANONYMOUS("비회원"),
	ROLE_MEMBER("회원"),
	ROLE_ADMIN("관리자");

	private String roleDesc;

	public static RoleType findRole(String roleName) {
		return Arrays.stream(RoleType.values())
			.filter(role -> role.name().equals(roleName))
			.findFirst()
			.orElse(ROLE_ANONYMOUS);
	}
}
