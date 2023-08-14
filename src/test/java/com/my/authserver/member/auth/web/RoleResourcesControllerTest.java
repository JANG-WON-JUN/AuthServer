package com.my.authserver.member.auth.web;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static com.my.authserver.member.enums.RoleType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.web.request.RoleResourceCreateRequest;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.support.controller.ControllerTestSupport;

class RoleResourcesControllerTest extends ControllerTestSupport {

	@Test
	@DisplayName("권한 id와 자원 id를 입력받아 자원별 권한을 생성할 수 있다.")
	void createRoleResource() throws Exception {
		// given
		RoleResourceCreateRequest request = createRequest(1L, 1L);

		// expected
		mockMvc.perform(post("/admin/api/roleResources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists());
	}

	@Test
	@DisplayName("자원별 권한을 생성 시 권한 id는 필수 입력이다.")
	void createRoleResourcesWithNoRoleId() throws Exception {
		// given
		RoleResourceCreateRequest request = createRequest(null, 1L);

		// expected
		mockMvc.perform(post("/admin/api/roleResources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("자원별 권한을 생성 시 자원 id는 필수 입력이다.")
	void createRoleResourcesWithNoResourceId() throws Exception {
		// given
		RoleResourceCreateRequest request = createRequest(1L, null);

		// expected
		mockMvc.perform(post("/admin/api/roleResources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("자원별 권한 id를 받아 삭제할 수 있다.")
	void deleteRoleResource() throws Exception {
		// given
		// expected
		mockMvc.perform(delete("/admin/api/roleResources/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("권한 이름으로 자원별 권한 리스트를 조회할 수 있다.")
	void findRoleResourcesWithCondition() throws Exception {
		// given
		RoleResourceSearchCondition condition = createRoleResourceSearchCondition(List.of(ROLE_ADMIN));

		given(roleResourceQueryService.findRoleResources(any(RoleResourceSearchCondition.class)))
			.willReturn(createPage());

		// expected
		mockMvc.perform(get("/admin/api/roleResources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(condition)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.content.size()").value(1));
	}

	@Test
	@DisplayName("조회 조건을 만족하는 자원별 권한이 없으면 조회결과는 없다.")
	void findRoleResourcesWithConditionNoResult() throws Exception {
		// given
		RoleResourceSearchCondition condition = createRoleResourceSearchCondition(List.of(ROLE_ADMIN));

		given(roleResourceQueryService.findRoleResources(any(RoleResourceSearchCondition.class)))
			.willReturn(Page.empty());

		// expected
		mockMvc.perform(get("/admin/api/roleResources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(condition)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.content.size()").value(0));
	}

	private RoleResourceCreateRequest createRequest(Long roleId, Long resourceId) {
		RoleResourceCreateRequest request = new RoleResourceCreateRequest();

		request.setRoleId(roleId);
		request.setResourceId(resourceId);

		return request;
	}

	private RoleResourceSearchCondition createRoleResourceSearchCondition(List<RoleType> roleTypes) {
		return RoleResourceSearchCondition.builder()
			.page(0)
			.roleTypes(roleTypes)
			.build();
	}

	private Page<RoleResource> createPage() {
		Role role = createRole(ROLE_ADMIN);
		Resource resource = createResource("/resource", GET, URL);
		RoleResource roleResource = createRoleResource(role, resource);

		List<RoleResource> roles = List.of(roleResource);

		return new PageImpl<>(roles, Pageable.ofSize(10), 0);
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
}