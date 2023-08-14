package com.my.authserver.member.auth.service.request;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResourceCreateServiceRequest {

	private String resourceName;

	private HttpMethod httpMethod;

	private ResourceType resourceType;

	@Builder
	private ResourceCreateServiceRequest(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		this.resourceName = resourceName;
		this.httpMethod = httpMethod;
		this.resourceType = resourceType;
	}

	public Resource toEntity() {
		return Resource.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}
}
