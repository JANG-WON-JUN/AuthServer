package com.my.authserver.member.auth.service.request;

import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResourceUpdateServiceRequest {

	private Long id;

	private String resourceName;

	private HttpMethod httpMethod;

	private ResourceType resourceType;

	@Builder
	private ResourceUpdateServiceRequest(Long id, String resourceName, HttpMethod httpMethod,
		ResourceType resourceType) {
		this.id = id;
		this.resourceName = resourceName;
		this.httpMethod = httpMethod;
		this.resourceType = resourceType;
	}
}
