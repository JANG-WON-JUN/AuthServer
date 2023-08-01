package com.my.authserver.member.auth.web.request;

import com.my.authserver.member.auth.service.request.RoleHierarchyCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleHierarchyCreateRequest {

	private RoleType parent;

	@NotNull(message = "{field.required.childRoleType}")
	private RoleType child;

	public RoleHierarchyCreateServiceRequest toServiceRequest() {
		return RoleHierarchyCreateServiceRequest.builder()
			.parent(parent)
			.child(child)
			.build();
	}
}
