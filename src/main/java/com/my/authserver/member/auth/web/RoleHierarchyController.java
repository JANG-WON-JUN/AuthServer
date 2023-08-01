package com.my.authserver.member.auth.web;

import static com.my.authserver.common.web.dto.Result.of;
import static org.springframework.http.ResponseEntity.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.authserver.common.web.dto.Result;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.web.request.RoleHierarchyCreateRequest;
import com.my.authserver.member.auth.web.response.RoleHierarchyResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleHierarchyController {

	private final RoleHierarchyService roleHierarchyService;
	private final RoleHierarchyQueryService roleHierarchyQueryService;

	@PostMapping("/api/roleHierarchies")
	public ResponseEntity<Result<Long>> createRoleHierarchy(@Valid @RequestBody RoleHierarchyCreateRequest request) {
		Long savedRoleHierarchyId = roleHierarchyService.createRoleHierarchy(request.toServiceRequest());
		return ok(of(savedRoleHierarchyId));
	}

	@GetMapping("/api/roleHierarchies")
	public ResponseEntity<Result<List<RoleHierarchyResponse>>> findRoleHierarchies() {
		List<RoleHierarchy> roleHierarchies = roleHierarchyQueryService.findRoleHierarchies();

		List<RoleHierarchyResponse> roleHierarchyResponses = roleHierarchies.stream()
			.map(RoleHierarchyResponse::of)
			.toList();

		return ok(of(roleHierarchyResponses));
	}

	@DeleteMapping("/api/roleHierarchies/{id}")
	public void deleteRoleHierarchy(@PathVariable Long id) {
		roleHierarchyService.deleteRoleHierarchy(id);
	}

}
