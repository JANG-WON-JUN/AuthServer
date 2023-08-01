package com.my.authserver.member.auth.web;

import static com.my.authserver.member.enums.RoleType.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.util.ReflectionTestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleHierarchyNotValid;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.service.request.RoleHierarchyCreateServiceRequest;
import com.my.authserver.member.auth.web.request.RoleHierarchyCreateRequest;
import com.my.authserver.member.enums.RoleType;

@WebMvcTest(controllers = RoleHierarchyController.class)
// 스프링 시큐리티를 사용하지 않을 때 필터 제외
@AutoConfigureMockMvc(addFilters = false)
class RoleHierarchyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RoleHierarchyService roleHierarchyService;

	@MockBean
	private RoleHierarchyQueryService roleHierarchyQueryService;

	@MockBean
	private MessageSourceUtils messageSourceUtils;

	@Test
	@DisplayName("권한 계층 생성 시 필요한 정보를 받아 권한 계층을 생성한다.")
	void createRoleHierarchy() throws Exception {
		// given
		RoleHierarchyCreateRequest request = createRoleHierarchyRequest(ROLE_ADMIN, ROLE_MEMBER);

		given(roleHierarchyService.createRoleHierarchy(any(RoleHierarchyCreateServiceRequest.class)))
			.willReturn(1L);

		// expected
		mockMvc.perform(post("/admin/api/roleHierarchies")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request.toServiceRequest())))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists());
	}

	@Test
	@DisplayName("권한 계층 생성 시 상위권한, 하위권한이 모두 null이면 예외가 발생한다.")
	void createRoleHierarchyWithNoRole() throws Exception {
		// given
		RoleHierarchyCreateRequest request = createRoleHierarchyRequest(null, null);

		given(roleHierarchyService.createRoleHierarchy(any(RoleHierarchyCreateServiceRequest.class)))
			.willThrow(RoleHierarchyNotValid.class);

		// expected
		mockMvc.perform(post("/admin/api/roleHierarchies")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request.toServiceRequest())))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("권한 계층 전체 조회 시 데이터가 존재하면 리스트로 반환한다.")
	void findRoleHierarchies() throws Exception {
		// given
		Role parent = createRole(ROLE_ADMIN);
		Role child = createRole(ROLE_MEMBER);

		RoleHierarchy roleHierarchy1 = createRoleHierarchy(parent, child);
		RoleHierarchy roleHierarchy2 = createRoleHierarchy(parent, child);
		RoleHierarchy roleHierarchy3 = createRoleHierarchy(parent, child);

		given(roleHierarchyQueryService.findRoleHierarchies())
			.willReturn(List.of(roleHierarchy1, roleHierarchy2, roleHierarchy3));

		// expected
		mockMvc.perform(get("/admin/api/roleHierarchies")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.size()").value(3));
	}

	@Test
	@DisplayName("권한 계층 전체 조회 시 데이터가 존재하지 않으면 비어있는 리스트를 반환한다.")
	void findRoleHierarchiesWithNoRoleHierarchy() throws Exception {
		// given
		given(roleHierarchyQueryService.findRoleHierarchies())
			.willReturn(List.of());

		// expected
		mockMvc.perform(get("/admin/api/roleHierarchies")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.size()").value(0));
	}

	@Test
	@DisplayName("권한 계층 아이디를 입력받아 권한을 삭제한다.")
	void deleteRole() throws Exception {
		// given

		// expected
		mockMvc.perform(delete("/admin/api/roleHierarchies/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	private RoleHierarchyCreateRequest createRoleHierarchyRequest(RoleType parent, RoleType child) {
		RoleHierarchyCreateRequest request = new RoleHierarchyCreateRequest();

		request.setParent(parent);
		request.setChild(child);

		return request;
	}

	private RoleHierarchy createRoleHierarchy(Role parent, Role child) {
		RoleHierarchy roleHierarchy = RoleHierarchy.create(parent, child);

		setField(roleHierarchy, "id", 1L);

		return roleHierarchy;
	}

	private Role createRole(RoleType roleType) {
		Role role = Role.builder()
			.roleType(roleType)
			.roleDesc(roleType.getRoleDesc())
			.build();

		setField(role, "id", 1L);

		return role;
	}
}