package com.my.authserver.member.auth.web.request;

import com.my.authserver.member.auth.service.request.ResourceCreateServiceRequest;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceCreateRequest {

	@NotBlank(message = "{field.required.resourceName}")
	private String resourceName;

	@NotNull(message = "{field.required.httpMethod}")
	private HttpMethod httpMethod;

	@NotNull(message = "{field.required.resourceType}")
	private ResourceType resourceType;

	public ResourceCreateServiceRequest toServiceRequest() {
		return ResourceCreateServiceRequest.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}
}
