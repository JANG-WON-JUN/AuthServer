package com.my.authserver.member.auth.web;

import com.my.authserver.common.web.dto.ApiResponse;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.web.request.RoleHierarchyCreateRequest;
import com.my.authserver.member.auth.web.response.RoleHierarchyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.my.authserver.common.web.dto.ApiResponse.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleHierarchyController {

    private final RoleHierarchyService roleHierarchyService;
    private final RoleHierarchyQueryService roleHierarchyQueryService;

    @PostMapping("/api/roleHierarchies")
    public ApiResponse<Long> createRoleHierarchy(@Valid @RequestBody RoleHierarchyCreateRequest request) {
        Long savedRoleHierarchyId = roleHierarchyService.createRoleHierarchy(request.toServiceRequest());
        return ok(savedRoleHierarchyId);
    }

    @GetMapping("/api/roleHierarchies")
    public ApiResponse<List<RoleHierarchyResponse>> findRoleHierarchies() {
        List<RoleHierarchy> roleHierarchies = roleHierarchyQueryService.findRoleHierarchies();

        List<RoleHierarchyResponse> roleHierarchyResponses = roleHierarchies.stream()
                .map(RoleHierarchyResponse::of)
                .toList();

        return ok(roleHierarchyResponses);
    }

    @DeleteMapping("/api/roleHierarchies/{id}")
    public void deleteRoleHierarchy(@PathVariable Long id) {
        roleHierarchyService.deleteRoleHierarchy(id);
    }

}
