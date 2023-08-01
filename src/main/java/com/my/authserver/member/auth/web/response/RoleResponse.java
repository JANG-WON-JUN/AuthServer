package com.my.authserver.member.auth.web.response;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.enums.RoleType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleResponse {

	private Long id;
	private RoleType roleType;
	private String roleDesc;

	@Builder
	private RoleResponse(Long id, RoleType roleType, String roleDesc) {
		this.id = id;
		this.roleType = roleType;
		this.roleDesc = roleDesc;
	}

	public static RoleResponse of(Role role) {
		return RoleResponse.builder()
			.id(role.getId())
			.roleType(role.getRoleType())
			.roleDesc(role.getRoleDesc())
			.build();
	}
}
