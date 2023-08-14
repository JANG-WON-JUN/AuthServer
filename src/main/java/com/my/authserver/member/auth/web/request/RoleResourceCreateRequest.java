package com.my.authserver.member.auth.web.request;

import com.my.authserver.member.auth.service.request.RoleResourceCreateServiceRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleResourceCreateRequest {

	@NotNull(message = "{field.required.role}")
	private Long roleId;

	@NotNull(message = "{field.required.resource}")
	private Long resourceId;

	public RoleResourceCreateServiceRequest toServiceRequest() {
		return RoleResourceCreateServiceRequest.builder()
			.roleId(roleId)
			.resourceId(resourceId)
			.build();
	}
}
