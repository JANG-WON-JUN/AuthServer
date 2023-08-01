package com.my.authserver.member.auth.service.request;

import com.my.authserver.member.enums.RoleType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleHierarchyCreateServiceRequest {

	private RoleType parent;
	private RoleType child;

	@Builder
	private RoleHierarchyCreateServiceRequest(RoleType parent, RoleType child) {
		this.parent = parent;
		this.child = child;
	}
}
