package com.my.authserver.member.web;

import static java.time.LocalDateTime.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.util.ReflectionTestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Period;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.PasswordNotFound;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.service.PasswordService;
import com.my.authserver.member.service.query.PasswordQueryService;
import com.my.authserver.member.service.request.PasswordUpdateServiceRequest;
import com.my.authserver.member.web.request.PasswordUpdateRequest;

@WebMvcTest(controllers = PasswordController.class)
// 스프링 시큐리티를 사용하지 않을 때 필터 제외
@AutoConfigureMockMvc(addFilters = false)
class PasswordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private PasswordService passwordService;

	@MockBean
	private PasswordQueryService passwordQueryService;

	@MockBean
	private MessageSourceUtils messageSourceUtils;

	@Test
	@DisplayName("아이디로 패스워드를 조회한다.")
	void findPassword() throws Exception {
		// given
		Password password = Password.create("비밀번호", MIN, 0, Period.ofMonths(3));
		Long passwordId = 1L;

		setField(password, "id", passwordId);

		given(passwordQueryService.findById(anyLong()))
			.willReturn(password);

		// expected
		mockMvc.perform(get("/member/api/password/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.id").value(passwordId));
	}

	@Test
	@DisplayName("존재하지 않은 아이디로 패스워드를 조회할 수 없다.")
	void findPasswordWithNotValid() throws Exception {
		// given
		given(passwordQueryService.findById(anyLong()))
			.willThrow(new PasswordNotFound(""));

		// expected
		mockMvc.perform(get("/member/api/password/1000")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("새로운 비밀번호를 입력받아 비밀번호를 수정한다.")
	void updatePassword() throws Exception {
		// given
		Long passwordId = 1L;
		PasswordUpdateRequest request = updateRequest(passwordId, "기존 비밀번호", "새로운 비밀번호");

		given(passwordService.updatePassword(any(PasswordUpdateServiceRequest.class)))
			.willReturn(passwordId);

		// expected
		mockMvc.perform(patch("/member/api/password")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").value(passwordId));
	}

	@Test
	@DisplayName("비밀번호 수정 시 아이디는 필수 입력이다.")
	void updatePasswordWithNoId() throws Exception {
		// given
		PasswordUpdateRequest request = updateRequest(null, "기존 비밀번호", "새로운 비밀번호");

		// expected
		mockMvc.perform(patch("/member/api/password")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("비밀번호 수정 시 기존 비밀번호는 필수 입력이다.")
	void updatePasswordWithNoPassword() throws Exception {
		// given
		PasswordUpdateRequest request = updateRequest(1L, null, "새로운 비밀번호");

		// expected
		mockMvc.perform(patch("/member/api/password")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("비밀번호 수정 시 새로운 비밀번호는 필수 입력이다.")
	void updatePasswordWithNoNewPassword() throws Exception {
		// given
		PasswordUpdateRequest request = updateRequest(1L, "기존 비밀번호", null);

		// expected
		mockMvc.perform(patch("/member/api/password")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	private PasswordUpdateRequest updateRequest(Long id, String password, String newPassword) {
		PasswordUpdateRequest request = new PasswordUpdateRequest();

		request.setId(id);
		request.setPassword(password);
		request.setNewPassword(newPassword);

		return request;
	}
}