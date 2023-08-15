package com.my.authserver.member.auth.web;

import static com.my.authserver.common.web.dto.ApiResponse.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.authserver.common.web.dto.ApiResponse;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.web.request.RoleCreateRequest;
import com.my.authserver.member.auth.web.request.RoleUpdateRequest;
import com.my.authserver.member.auth.web.response.RoleResponse;
import com.my.authserver.member.auth.web.searchcondition.RoleSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleController {

	private final RoleService roleService;
	private final RoleQueryService roleQueryService;

	@PostMapping("/api/roles")
	public ApiResponse<Long> createRole(@Valid @RequestBody RoleCreateRequest request) {
		Long savedRoleId = roleService.createRole(request.toServiceRequest());
		return ok(savedRoleId);
	}

	@GetMapping("/api/roles/{id}")
	public ApiResponse<RoleResponse> findRole(@PathVariable Long id) {
		Role savedRole = roleQueryService.findById(id);
		RoleResponse savedRoleResponse = RoleResponse.from(savedRole);
		return ok(savedRoleResponse);
	}

	@GetMapping("/api/roles")
	public ApiResponse<Page<RoleResponse>> findRoles(@RequestBody RoleSearchCondition condition) {
		Page<RoleResponse> responsePage = roleQueryService.findRolesWithCondition(condition)
			.map(RoleResponse::from);

		return ok(responsePage);
	}

	@PatchMapping("/api/roles")
	public ApiResponse<Long> updateRole(@Valid @RequestBody RoleUpdateRequest request) {
		Long savedRoleId = roleService.updateRole(request.toServiceRequest());
		return ok(savedRoleId);
	}

	@DeleteMapping("/api/roles/{id}")
	public ApiResponse deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
		return ok();
	}
}
