package com.my.authserver.member.auth.web;

import static com.my.authserver.member.enums.RoleType.*;
import static java.nio.charset.StandardCharsets.*;
import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.dto.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.web.request.RoleCreateRequest;
import com.my.authserver.member.auth.web.request.RoleSearchCondition;
import com.my.authserver.member.auth.web.request.RoleUpdateRequest;
import com.my.authserver.member.enums.RoleType;

@WebMvcTest(value = RoleController.class)
// 스프링 시큐리티를 사용하지 않을 떄 필터 제외
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RoleService roleService;

	@MockBean
	private RoleQueryService roleQueryService;

	@MockBean
	private MessageSourceUtils messageSourceUtils;

	@Test
	@DisplayName("권한 생성 시 필요한 정보를 받아 권한을 생성한다.")
	void createSavedRole() throws Exception {
		// given
		RoleCreateRequest request = createRoleRequest(ROLE_ADMIN);

		// expected
		mockMvc.perform(post("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists());
	}

	@Test
	@DisplayName("권한 생성 시 권한 타입이 입력되지 않으면 예외가 발생한다.")
	void createRoleWithNoRoleType() throws Exception {
		// given
		RoleCreateRequest request = createRoleRequest(null, "권한 설명");

		// expected
		mockMvc.perform(post("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("권한 생성 시 권한 설명이 입력하지 않으면 예외가 발생한다.")
	void createRoleWithNoRoleDesc() throws Exception {
		// given
		RoleCreateRequest request = createRoleRequest(ROLE_ADMIN, "   ");

		// expected
		mockMvc.perform(post("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("권한 아이디를 입력받아 권한 1개를 조회한다.")
	void findRole() throws Exception {
		// given
		Role role = createSavedRole(ROLE_ANONYMOUS);

		given(roleQueryService.findById(anyLong()))
			.willReturn(role);

		// expected
		mockMvc.perform(get("/admin/api/roles/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.roleType").value(role.getRoleType().toString()))
			.andExpect(jsonPath("data.roleDesc").value(role.getRoleDesc()));
	}

	@Test
	@DisplayName("존재하지 않는 권한 아이디를 입력받아 권한 1개를 조회하면 예외가 발생한다.")
	void findRoleWithNoId() throws Exception {
		// given
		given(roleQueryService.findById(anyLong()))
			.willThrow(new RoleNotFound(""));

		// expected
		mockMvc.perform(get("/admin/api/roles/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("조회 조건을 적용하여 권한 목록을 조회한다.")
	void findRolesWithCondition() throws Exception {
		// given
		saveRoles(List.of(ROLE_ANONYMOUS, ROLE_MEMBER, ROLE_ADMIN));

		RoleSearchCondition condition = createRoleSearchCondition(null);

		given(roleQueryService.findRolesWithCondition(any(RoleSearchCondition.class)))
			.willReturn(createPage(List.of()));

		// expected
		mockMvc.perform(get("/admin/api/roles")
				.characterEncoding(UTF_8)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(condition)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.size()").value(0));
	}

	@Test
	@DisplayName("권한 설명을 조회 조건으로 받아 조건을 만족하는 권한 목록을 조회한다.")
	void findRolesWithConditionWithKeyword() throws Exception {
		// given
		saveRoles(List.of(ROLE_ANONYMOUS, ROLE_MEMBER, ROLE_ADMIN));

		RoleSearchCondition condition = createRoleSearchCondition("회원");

		given(roleQueryService.findRolesWithCondition(any(RoleSearchCondition.class)))
			.willReturn(createPage(List.of(ROLE_ANONYMOUS, ROLE_MEMBER)));

		// expected
		mockMvc.perform(get("/admin/api/roles")
				.characterEncoding(UTF_8)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(condition)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.size()").value(2));
	}

	@Test
	@DisplayName("권한의 id와 새로운 권한 설명을 받아 권한 설명을 수정한다.")
	void updateRole() throws Exception {
		// given
		Role role = createSavedRole(ROLE_ANONYMOUS);
		RoleUpdateRequest updateRequest = updateRequest("새로운 비회원 설명");

		given(roleQueryService.findById(anyLong()))
			.willReturn(role);

		// expected
		mockMvc.perform(patch("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("권한 설명을 수정 시 권한 설명은 필수입력이다.")
	void updateRoleWithNoRoleDesc() throws Exception {
		// given
		Role role = createSavedRole(ROLE_ANONYMOUS);
		RoleUpdateRequest updateRequest = updateRequest(" ");

		given(roleQueryService.findById(anyLong()))
			.willReturn(role);

		// expected
		mockMvc.perform(patch("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("권한 아이디를 입력받아 권한을 삭제한다.")
	void deleteRole() throws Exception {
		// given

		// expected
		mockMvc.perform(delete("/admin/api/roles/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	private RoleCreateRequest createRoleRequest(RoleType roleType) {
		RoleCreateRequest request = new RoleCreateRequest();

		request.setRoleType(roleType);
		request.setRoleDesc(roleType.getRoleDesc());

		return request;
	}

	private RoleCreateRequest createRoleRequest(RoleType roleType, String roleDesc) {
		RoleCreateRequest request = new RoleCreateRequest();

		request.setRoleType(roleType);
		request.setRoleDesc(roleDesc);

		return request;
	}

	private Role createSavedRole(RoleType roleType) {
		Role role = Role.builder()
			.roleType(roleType)
			.roleDesc(roleType.getRoleDesc())
			.build();

		setField(role, "id", 1L);

		return role;
	}

	private void saveRoles(List<RoleType> roleTypes) {
		roleTypes.stream()
			.map(this::createRoleRequest)
			.forEach(role -> roleService.createRole(role.toServiceRequest()));
	}

	private RoleSearchCondition createRoleSearchCondition(String keyword) {
		return RoleSearchCondition.builder()
			.page(0)
			.keyword(keyword)
			.build();
	}

	private Page<Role> createPage(List<RoleType> roleTypes) {
		List<Role> roles = roleTypes.stream()
			.map(this::createSavedRole)
			.toList();

		return new PageImpl<>(roles, Pageable.ofSize(10), 0);
	}

	private RoleUpdateRequest updateRequest(String roleDesc) {
		RoleUpdateRequest request = new RoleUpdateRequest();

		request.setId(1L);
		request.setRoleDesc(roleDesc);

		return request;
	}
}