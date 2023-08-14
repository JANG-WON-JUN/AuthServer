package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static com.my.authserver.member.enums.RoleType.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import com.my.authserver.common.web.exception.RoleNotFound;
import com.my.authserver.common.web.exception.RoleResourceNotFound;
import com.my.authserver.common.web.exception.dto.ResourceNotFound;
import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.service.request.RoleResourceCreateServiceRequest;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.support.service.ServiceTestSupport;

class RoleResourceServiceTest extends ServiceTestSupport {

	@Test
	@DisplayName("권한 id와 자원 id를 입력받아 자원별 권한을 생성할 수 있다.")
	void createRoleResources() {
		// given
		Role role = createRole(ROLE_ADMIN);
		Resource resource = createResource("/resource", GET, URL);

		Role savedRole = roleRepository.save(role);
		Resource savedResource = resourcesRepository.save(resource);

		RoleResourceCreateServiceRequest request = createRequest(savedRole.getId(), savedResource.getId());

		// when
		Long savedRoleResourceId = roleResourceService.createRoleResource(request);

		// then
		RoleResource savedRoleResource = roleResourceQueryService.findById(savedRoleResourceId);
		assertThat(savedRoleResource).isNotNull();
		assertThat(savedRoleResource.getRole()).isEqualTo(savedRole);
		assertThat(savedRoleResource.getResource()).isEqualTo(savedResource);
	}

	@Test
	@DisplayName("자원별 권한을 생성 시 권한 id는 필수 입력이다.")
	void createRoleResourcesWithNoRoleId() {
		// given
		RoleResourceCreateServiceRequest request = createRequest(null, 1L);

		// expected
		assertThatThrownBy(() -> roleResourceService.createRoleResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.role"));
	}

	@Test
	@DisplayName("자원별 권한을 생성 시 자원 id는 필수 입력이다.")
	void createRoleResourcesWithNoResourceId() {
		// given
		RoleResourceCreateServiceRequest request = createRequest(1L, null);

		// expected
		assertThatThrownBy(() -> roleResourceService.createRoleResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.resource"));
	}

	@Test
	@DisplayName("존재하지 않는 권한 id로 자원별 권한을 생성할 수 없다.")
	void createRoleResourcesWithNotValidRoleId() {
		// given
		Resource resource = createResource("/resource", GET, URL);
		Resource savedResource = resourcesRepository.save(resource);

		RoleResourceCreateServiceRequest request = createRequest(1L, savedResource.getId());

		// expected
		assertThatThrownBy(() -> roleResourceService.createRoleResource(request))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	@Test
	@DisplayName("존재하지 않는 자원 id로 자원별 권한을 생성할 수 없다.")
	void createRoleResourcesWithNotValidResourceId() {
		// given
		Role role = createRole(ROLE_ADMIN);
		Role savedRole = roleRepository.save(role);

		RoleResourceCreateServiceRequest request = createRequest(savedRole.getId(), 1L);

		// expected
		assertThatThrownBy(() -> roleResourceService.createRoleResource(request))
			.isInstanceOf(ResourceNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noResource"));
	}

	@Test
	@DisplayName("권한 이름으로 자원별 권한 리스트를 조회할 수 있다.")
	void findByRoleType() {
		// given
		Role role1 = createRole(ROLE_ADMIN);
		Role role2 = createRole(ROLE_MEMBER);
		Role role3 = createRole(ROLE_ANONYMOUS);

		Resource resource = createResource("/resource", GET, URL);

		RoleResource roleResource1 = createRoleResource(role1, resource);
		RoleResource roleResource2 = createRoleResource(role2, resource);
		RoleResource roleResource3 = createRoleResource(role3, resource);

		roleRepository.saveAll(List.of(role1, role2, role3));
		resourcesRepository.save(resource);
		roleResourceRepository.saveAll(List.of(roleResource1, roleResource2, roleResource3));

		// when
		List<RoleResource> roleResources = roleResourceQueryService.findByRoleType(ROLE_ADMIN);

		// then
		assertThat(roleResources).hasSize(1)
			.extracting("role", "resource")
			.containsExactlyInAnyOrder(
				tuple(role1, resource)
			);
	}

	@Test
	@DisplayName("자원별 권한 id를 받아 삭제할 수 있다.")
	void deleteRoleResource() {
		// given
		Role role = createRole(ROLE_ADMIN);
		Resource resource = createResource("/resource", GET, URL);

		Role savedRole = roleRepository.save(role);
		Resource savedResource = resourcesRepository.save(resource);
		RoleResourceCreateServiceRequest request = createRequest(savedRole.getId(), savedResource.getId());
		Long savedRoleResourceId = roleResourceService.createRoleResource(request);

		// when
		roleResourceService.deleteRoleResource(savedRoleResourceId);

		// then
		assertThat(roleResourceRepository.count()).isZero();
	}

	@Test
	@DisplayName("존재하지 않는 자원별 권한 id로 삭제할 수 없다.")
	void deleteRoleResourceWithNotValidId() {
		// given
		// when
		assertThatThrownBy(() -> roleResourceService.deleteRoleResource(1L))
			.isInstanceOf(RoleResourceNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleResource"));
	}

	@Test
	@DisplayName("권한 이름으로 자원별 권한 리스트를 조회할 수 있다.")
	void findRoleResourcesWithCondition() {
		// given
		Role role1 = createRole(ROLE_ADMIN);
		Role role2 = createRole(ROLE_MEMBER);
		Role role3 = createRole(ROLE_ANONYMOUS);

		Resource resource = createResource("/resource", GET, URL);

		RoleResource roleResource1 = createRoleResource(role1, resource);
		RoleResource roleResource2 = createRoleResource(role2, resource);
		RoleResource roleResource3 = createRoleResource(role3, resource);

		roleRepository.saveAll(List.of(role1, role2, role3));
		resourcesRepository.save(resource);
		roleResourceRepository.saveAll(List.of(roleResource1, roleResource2, roleResource3));

		RoleResourceSearchCondition condition = createRoleResourceSearchCondition(List.of(ROLE_ADMIN));

		// when
		Page<RoleResource> page = roleResourceQueryService.findRoleResources(condition);

		// then
		List<RoleResource> roleResources = page.getContent();
		assertThat(roleResources).hasSize(1)
			.extracting("role", "resource")
			.containsExactlyInAnyOrder(
				tuple(role1, resource)
			);
	}

	@Test
	@DisplayName("일치하는 권한 이름이 없으면 조회 결과는 없다.")
	void findRoleResourcesWithConditionNoResult() {
		// given
		RoleResourceSearchCondition condition = createRoleResourceSearchCondition(emptyList());

		// when
		Page<RoleResource> page = roleResourceQueryService.findRoleResources(condition);

		// then
		List<RoleResource> roleResources = page.getContent();
		assertThat(roleResources).hasSize(0);
	}

	private RoleResourceCreateServiceRequest createRequest(Long roleId, Long resourceId) {
		return RoleResourceCreateServiceRequest.builder()
			.roleId(roleId)
			.resourceId(resourceId)
			.build();
	}

	private RoleResource createRoleResource(Role role, Resource resource) {
		return RoleResource.builder()
			.role(role)
			.resource(resource)
			.build();
	}

	private Role createRole(RoleType roleType) {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleType.getRoleDesc())
			.build();
	}

	private Resource createResource(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		return Resource.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}

	private RoleResourceSearchCondition createRoleResourceSearchCondition(List<RoleType> roleTypes) {
		return RoleResourceSearchCondition.builder()
			.page(0)
			.roleTypes(roleTypes)
			.build();
	}
}