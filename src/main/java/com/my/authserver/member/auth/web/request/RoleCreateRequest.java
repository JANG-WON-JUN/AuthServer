package com.my.authserver.member.auth.web.request;

import com.my.authserver.member.auth.service.request.RoleCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleCreateRequest {

	@NotNull(message = "{field.required.roleType}")
	private RoleType roleType;

	@NotBlank(message = "{field.required.roleDesc}")
	private String roleDesc;

	public RoleCreateServiceRequest toServiceRequest() {
		return RoleCreateServiceRequest.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}
}
