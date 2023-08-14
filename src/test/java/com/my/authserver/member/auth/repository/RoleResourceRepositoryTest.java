package com.my.authserver.member.auth.repository;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static com.my.authserver.member.enums.RoleType.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.support.repository.RepositoryTestSupport;

class RoleResourceRepositoryTest extends RepositoryTestSupport {

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
		List<RoleResource> roleResources = roleResourceRepository.findByRoleType(ROLE_ADMIN);

		// then
		assertThat(roleResources).hasSize(1)
			.extracting("role", "resource")
			.containsExactlyInAnyOrder(
				tuple(role1, resource)
			);
	}

	@Test
	@DisplayName("일치하는 권한 이름이 없으면 조회 결과는 없다.")
	void findByRoleTypeWithNoResult() {
		// given
		Role role = createRole(ROLE_ADMIN);
		Resource resource = createResource("/resource", GET, URL);

		RoleResource roleResource = createRoleResource(role, resource);

		roleRepository.save(role);
		resourcesRepository.save(resource);
		roleResourceRepository.save(roleResource);

		// when
		List<RoleResource> roleResources = roleResourceRepository.findByRoleType(ROLE_MEMBER);

		// then
		assertThat(roleResources).hasSize(0);
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
		Page<RoleResource> page = roleResourceRepository.findRoleResourcesWithCondition(condition);

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
		Page<RoleResource> page = roleResourceRepository.findRoleResourcesWithCondition(condition);

		// then
		List<RoleResource> roleResources = page.getContent();
		assertThat(roleResources).hasSize(0);
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