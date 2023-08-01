package com.my.authserver.member.auth.web.response;

import com.my.authserver.domain.entity.member.auth.RoleHierarchy;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleHierarchyResponse {

	private Long id;
	private RoleResponse parent;
	private RoleResponse child;

	@Builder
	private RoleHierarchyResponse(Long id, RoleResponse parent, RoleResponse child) {
		this.id = id;
		this.parent = parent;
		this.child = child;
	}

	public static RoleHierarchyResponse of(RoleHierarchy roleHierarchy) {
		return RoleHierarchyResponse.builder()
			.id(roleHierarchy.getId())
			.parent(RoleResponse.of(roleHierarchy.getParent()))
			.child(RoleResponse.of(roleHierarchy.getChild()))
			.build();
	}
}
