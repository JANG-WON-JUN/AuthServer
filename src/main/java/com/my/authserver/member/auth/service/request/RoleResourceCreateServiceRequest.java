package com.my.authserver.member.auth.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleResourceCreateServiceRequest {

	private Long roleId;

	private Long resourceId;

	@Builder
	private RoleResourceCreateServiceRequest(Long roleId, Long resourceId) {
		this.roleId = roleId;
		this.resourceId = resourceId;
	}
}
