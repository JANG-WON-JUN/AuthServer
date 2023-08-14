package com.my.authserver.docs.member.auth;

import static com.my.authserver.member.enums.RoleType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;

import com.my.authserver.docs.RestDocsSupport;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.service.request.RoleCreateServiceRequest;
import com.my.authserver.member.auth.web.RoleController;
import com.my.authserver.member.auth.web.request.RoleCreateRequest;
import com.my.authserver.member.enums.RoleType;

public class RoleControllerDocsTest extends RestDocsSupport {

	private final RoleService roleService = mock(RoleService.class);
	private final RoleQueryService roleQueryService = mock(RoleQueryService.class);

	@Override
	protected Object initController() {
		return new RoleController(roleService, roleQueryService);
	}

	//@Test
	@DisplayName("권한 생성 시 필요한 정보를 받아 권한을 생성한다.")
	void createSavedRole() throws Exception {
		RoleCreateRequest request = createRoleRequest(ROLE_ADMIN);

		given(roleService.createRole(any(RoleCreateServiceRequest.class)))
			.willReturn(1L);

		mockMvc.perform(post("/admin/api/roles")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists())
			.andDo(document("role-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("roleType").type(STRING)
						.description("권한 타입"),
					fieldWithPath("roleDesc").type(STRING)
						.optional()
						.description("권한 설명")
				),
				responseFields(
					fieldWithPath("data").type(NUMBER)
						.description("저장된 권한 아이디")
				)
			));
	}

	private RoleCreateRequest createRoleRequest(RoleType roleType) {
		RoleCreateRequest request = new RoleCreateRequest();

		request.setRoleType(roleType);
		request.setRoleDesc(roleType.getRoleDesc());

		return request;
	}

}
