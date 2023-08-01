package com.my.authserver.member.auth.web;

import static com.my.authserver.common.web.dto.Result.of;
import static org.springframework.http.ResponseEntity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.my.authserver.common.web.dto.Result;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.web.request.RoleCreateRequest;
import com.my.authserver.member.auth.web.request.RoleSearchCondition;
import com.my.authserver.member.auth.web.request.RoleUpdateRequest;
import com.my.authserver.member.auth.web.response.RoleResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleController {

	private final RoleService roleService;
	private final RoleQueryService roleQueryService;

	@PostMapping("/api/roles")
	public ResponseEntity<Result<Long>> createRole(@Valid @RequestBody RoleCreateRequest request) {
		Long savedRoleId = roleService.createRole(request.toServiceRequest());
		return ok(of(savedRoleId));
	}

	@GetMapping("/api/roles/{id}")
	public ResponseEntity<Result<RoleResponse>> findRole(@PathVariable Long id) {
		Role savedRole = roleQueryService.findById(id);
		RoleResponse savedRoleResponse = RoleResponse.of(savedRole);
		return ok(of(savedRoleResponse));
	}

	@GetMapping("/api/roles")
	public ResponseEntity<Result<List<RoleResponse>>> findRoles(@RequestBody RoleSearchCondition condition) {
		Page<Role> page = roleQueryService.findRolesWithCondition(condition);

		List<RoleResponse> roleResponses = page.getContent().stream()
			.map(RoleResponse::of)
			.toList();

		return ok(of(roleResponses));
	}

	@PatchMapping("/api/roles")
	public ResponseEntity<Result<Long>> updateRole(@Valid @RequestBody RoleUpdateRequest request) {
		Long savedRoleId = roleService.updateRole(request.toServiceRequest());
		return ok(of(savedRoleId));
	}

	@DeleteMapping("/api/roles/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
		return ok().build();
	}
}
