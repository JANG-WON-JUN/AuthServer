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
import com.my.authserver.member.auth.service.ResourceService;
import com.my.authserver.member.auth.service.query.ResourceQueryService;
import com.my.authserver.member.auth.web.request.ResourceCreateRequest;
import com.my.authserver.member.auth.web.request.ResourceUpdateRequest;
import com.my.authserver.member.auth.web.response.ResourceResponse;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ResourceController {

	private final ResourceService resourceService;
	private final ResourceQueryService resourceQueryService;

	@PostMapping("/api/resources")
	public ApiResponse<Long> createResource(@Valid @RequestBody ResourceCreateRequest request) {
		Long savedRoleId = resourceService.createResource(request.toServiceRequest());
		return ok(savedRoleId);
	}

	@GetMapping("/api/resources")
	public ApiResponse<Page<ResourceResponse>> findByResources(@RequestBody ResourceSearchCondition condition) {
		Page<ResourceResponse> responsePage = resourceQueryService.findResourcesWithCondition(condition)
			.map(ResourceResponse::of);

		return ok(responsePage);
	}

	@PatchMapping("/api/resources")
	public ApiResponse<Long> updateResource(@Valid @RequestBody ResourceUpdateRequest request) {
		Long savedResourceId = resourceService.updateResource(request.toServiceRequest());
		return ok(savedResourceId);
	}

	@DeleteMapping("/api/resources/{id}")
	public ApiResponse deleteResource(@PathVariable Long id) {
		resourceService.deleteResource(id);
		return ok();
	}
}
