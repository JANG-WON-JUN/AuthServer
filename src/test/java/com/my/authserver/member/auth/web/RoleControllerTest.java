package com.my.authserver.member.auth.web;

import static com.my.authserver.member.enums.RoleType.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.member.auth.service.RoleQueryService;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.member.web.request.RoleCreateRequest;

@WebMvcTest(value = RoleController.class, properties = "/messages/error.properties")
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
	void createRole() throws Exception {
		// given
		RoleType roleType = ROLE_ADMIN;
		RoleCreateRequest request = createRoleRequest(roleType, roleType.getRoleDesc());

		// expected
		mockMvc.perform(post("/admin/api/role")
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
		mockMvc.perform(post("/admin/api/role")
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
		mockMvc.perform(post("/admin/api/role")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("권한 아이디를 입력받아 권한 1개를 조회한다.")
	void findRole() throws Exception {
		// given

		// expected
		mockMvc.perform(delete("/admin/api/role/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("권한 아이디를 입력받아 권한을 삭제한다.")
	void deleteRole() throws Exception {
		// given

		// expected
		mockMvc.perform(delete("/admin/api/role/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	private RoleCreateRequest createRoleRequest(RoleType roleType, String roleDesc) {
		RoleCreateRequest request = new RoleCreateRequest();

		request.setRoleType(roleType);
		request.setRoleDesc(roleDesc);

		return request;
	}
}