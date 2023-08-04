package com.my.authserver.member.service.query;

import static com.my.authserver.member.service.request.PasswordCreateServiceRequest.*;
import static java.time.LocalDateTime.*;
import static java.time.Period.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.PasswordNotFound;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.service.PasswordService;

@MyServiceTest
class PasswordQueryServiceTest {

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private PasswordQueryService passwordQueryService;

	@Autowired
	private MessageSourceUtils messageSourceUtils;

	@Test
	@DisplayName("패스워드 식별자로 패스워드를 조회한다.")
	void findById() {
		// given
		Long savedPasswordId = passwordService.createPassword(create("myPassword", MIN, 1, ZERO));

		// when
		Password savedPassword = passwordQueryService.findById(savedPasswordId);

		// then
		assertThat(savedPassword).isNotNull();
		assertThat(savedPassword.getId()).isNotNull();
	}

	@Test
	@DisplayName("존재하지 않는 패스워드는 조회할 수 없다.")
	void findByIdWithNoId() {
		// given
		passwordService.createPassword(create("myPassword", MIN, 1, ZERO));

		// when
		assertThatThrownBy(() -> passwordQueryService.findById(0L))
			.isInstanceOf(PasswordNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noPassword"));
	}
}