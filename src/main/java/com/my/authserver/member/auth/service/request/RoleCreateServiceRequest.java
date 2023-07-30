package com.my.authserver.member.auth.service.request;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.enums.RoleType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoleCreateServiceRequest {

	private RoleType roleType;
	private String roleDesc;

	@Builder
	private RoleCreateServiceRequest(RoleType roleType, String roleDesc) {
		this.roleType = roleType;
		this.roleDesc = roleDesc;
	}

	public Role toEntity() {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}
}
