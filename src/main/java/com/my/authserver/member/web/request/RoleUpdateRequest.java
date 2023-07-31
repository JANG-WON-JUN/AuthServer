package com.my.authserver.member.web.request;

import com.my.authserver.member.auth.service.request.RoleUpdateServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleUpdateRequest {

	@NotNull(message = "{field.required.id}")
	private Long id;

	@NotBlank(message = "{field.required.roleDesc}")
	private String roleDesc;

	public RoleUpdateServiceRequest toServiceRequest() {
		return RoleUpdateServiceRequest.builder()
			.id(id)
			.roleDesc(roleDesc)
			.build();
	}

}
