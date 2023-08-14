package com.my.authserver.member.auth.web.response;

import com.my.authserver.domain.entity.member.auth.RoleResource;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleResourceResponse {

	private Long id;
	private RoleResponse role;
	private ResourceResponse resource;

	@Builder
	private RoleResourceResponse(Long id, RoleResponse role, ResourceResponse resource) {
		this.id = id;
		this.role = role;
		this.resource = resource;
	}

	public static RoleResourceResponse of(RoleResource roleResource) {
		return RoleResourceResponse.builder()
			.id(roleResource.getId())
			.role(RoleResponse.of(roleResource.getRole()))
			.resource(ResourceResponse.of(roleResource.getResource()))
			.build();
	}
}
