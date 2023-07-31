package com.my.authserver.member.auth.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleUpdateServiceRequest {

	private Long id;
	private String roleDesc;

	@Builder
	private RoleUpdateServiceRequest(Long id, String roleDesc) {
		this.id = id;
		this.roleDesc = roleDesc;
	}
}
