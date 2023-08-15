package com.my.authserver.member.auth.web.response;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResourceResponse {

	private Long id;
	private String resourceName;
	private HttpMethod httpMethod;
	private ResourceType resourceType;

	@Builder
	private ResourceResponse(Long id, String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		this.id = id;
		this.resourceName = resourceName;
		this.httpMethod = httpMethod;
		this.resourceType = resourceType;
	}

	public static ResourceResponse from(Resource resource) {
		return ResourceResponse.builder()
			.id(resource.getId())
			.resourceName(resource.getResourceName())
			.httpMethod(resource.getHttpMethod())
			.resourceType(resource.getResourceType())
			.build();
	}
}
