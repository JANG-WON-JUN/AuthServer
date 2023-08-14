package com.my.authserver.member.auth.web;

import static com.my.authserver.common.web.dto.ApiResponse.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.authserver.common.web.dto.ApiResponse;
import com.my.authserver.member.auth.service.RoleResourceService;
import com.my.authserver.member.auth.service.query.RoleResourceQueryService;
import com.my.authserver.member.auth.web.request.RoleResourceCreateRequest;
import com.my.authserver.member.auth.web.response.RoleResourceResponse;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleResourcesController {

	private final RoleResourceService roleResourceService;
	private final RoleResourceQueryService roleResourceQueryService;

	@PostMapping("/api/roleResources")
	public ApiResponse<Long> createRoleResource(@Valid @RequestBody RoleResourceCreateRequest request) {
		Long savedRoleResourceId = roleResourceService.createRoleResource(request.toServiceRequest());
		return ok(savedRoleResourceId);
	}

	@GetMapping("/api/roleResources")
	public ApiResponse<Page<RoleResourceResponse>> findResources(RoleResourceSearchCondition condition) {
		Page<RoleResourceResponse> responsePage =
			roleResourceQueryService.findRoleResources(condition).map(RoleResourceResponse::of);

		return ok(responsePage);
	}

	@DeleteMapping("/api/roleResources/{id}")
	public void deleteRoleResource(@PathVariable Long id) {
		roleResourceService.deleteRoleResource(id);
	}

}
