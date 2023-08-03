package com.my.authserver.member.service;

import static com.my.authserver.member.service.request.PasswordCreateServiceRequest.*;
import static java.time.LocalDateTime.*;
import static java.time.Period.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.PasswordNotMatched;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.service.query.PasswordQueryService;
import com.my.authserver.member.service.request.PasswordCreateServiceRequest;
import com.my.authserver.member.service.request.PasswordUpdateServiceRequest;

@MyServiceTest
class PasswordServiceTest {

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private PasswordQueryService passwordQueryService;

	@Autowired
	private MessageSourceUtils messageSourceUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("비멀번호를 생성한다.")
	void createPassword() {
		// given
		PasswordCreateServiceRequest request = create("비밀번호", MIN, 1, ZERO);

		// when
		Long savedPasswordId = passwordService.createPassword(request);

		// then
		Password savedPassword = passwordQueryService.findById(savedPasswordId);
		assertThat(savedPassword.getId()).isEqualTo(savedPasswordId);
	}

	@Test
	@DisplayName("비멀번호는 암호화 되어 생성된다.")
	void createPasswordWithEncoding() {
		// given
		String password = "myPassword";
		PasswordCreateServiceRequest request = create(password, MIN, 1, ZERO);

		// when
		Long savedPasswordId = passwordService.createPassword(request);

		// then
		Password savedPassword = passwordQueryService.findById(savedPasswordId);
		assertThat(passwordEncoder.matches(password, savedPassword.getPassword())).isTrue();
	}

	@Test
	@DisplayName("비멀번호는 필수 입력이다.")
	void createPasswordWithNoPassword() {
		// given
		PasswordCreateServiceRequest request = create(" ", MIN, 1, ZERO);

		// expected
		assertThatThrownBy(() -> passwordService.createPassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.password"));
	}

	@Test
	@DisplayName("마지막 변경일자는 필수 입력이다.")
	void createPasswordWithNoLastModDateTime() {
		// given
		PasswordCreateServiceRequest request = create("비밀번호", null, 1, ZERO);

		// expected
		assertThatThrownBy(() -> passwordService.createPassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.lastModDateTime"));
	}

	@Test
	@DisplayName("마지막 변경일자는 필수 입력이다.")
	void createPasswordWithNoLockLimitMinutes() {
		// given
		PasswordCreateServiceRequest request = create("비밀번호", MIN, null, ZERO);

		// expected
		assertThatThrownBy(() -> passwordService.createPassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.lockLimitMinutes"));
	}

	@Test
	@DisplayName("비밀번호 변경 주기는 필수 입력이다.")
	void createPasswordWithNoChangeCycle() {
		// given
		PasswordCreateServiceRequest request = create("비밀번호", MIN, 1, null);

		// expected
		assertThatThrownBy(() -> passwordService.createPassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.changeCycle"));
	}

	@Test
	@DisplayName("비밀번호, 비밀번호 확인을 입력하여 비밀번호를 변경한다.")
	void updatePassword() {
		// given
		String password = "myPassword";
		String newPassword = "새로운 비밀번호";
		Long passwordId = passwordService.createPassword(create(password, MIN, 1, ZERO));
		PasswordUpdateServiceRequest request = updateCreate(passwordId, password, newPassword, newPassword);

		// when
		Long savedPasswordId = passwordService.updatePassword(request);

		// then
		Password updatedPassword = passwordQueryService.findById(savedPasswordId);

		assertThat(updatedPassword.getId()).isEqualTo(passwordId);
		assertThat(passwordEncoder.matches(newPassword, updatedPassword.getPassword())).isTrue();
	}

	@Test
	@DisplayName("비밀번호 변경 시 기존 비멀번호는 필수 입력이다.")
	void updatePasswordWithNoPrevPassword() {
		// given
		Long passwordId = passwordService.createPassword(create("myPassword", MIN, 1, ZERO));
		PasswordUpdateServiceRequest request = updateCreate(passwordId, "  ", "새로운 비밀번호", "새로운 비밀번호");

		// expected
		assertThatThrownBy(() -> passwordService.updatePassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.password"));
	}

	@Test
	@DisplayName("비밀번호 변경 시 새로운 비멀번호는 필수 입력이다.")
	void updatePasswordWithNoNewPassword() {
		// given
		Long passwordId = passwordService.createPassword(create("myPassword", MIN, 1, ZERO));
		PasswordUpdateServiceRequest request = updateCreate(passwordId, "기존 비밀번호", "  ", "새로운 비밀번호");

		// expected
		assertThatThrownBy(() -> passwordService.updatePassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.newPassword"));
	}

	@Test
	@DisplayName("비밀번호 변경 시 비멀번호 확인 입력은 필수 입력이다.")
	void updatePasswordWithNoPasswordConfirm() {
		// given
		Long passwordId = passwordService.createPassword(create("myPassword", MIN, 1, ZERO));
		PasswordUpdateServiceRequest request = updateCreate(passwordId, "기존 비밀번호", "새로운 비밀번호", "");

		// expected
		assertThatThrownBy(() -> passwordService.updatePassword(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.passwordConfirm"));
	}

	@Test
	@DisplayName("비밀번호 변경 시 기존 비멀번호와 일치하지 않으면 변경할 수 없다.")
	void updatePasswordWithNotMatchedPrevPassword() {
		// given
		Long passwordId = passwordService.createPassword(create("myPassword", MIN, 1, ZERO));
		PasswordUpdateServiceRequest request = updateCreate(passwordId, "엉뚱한 비밀번호", "새로운 비밀번호", "새로운 비밀번호");

		// expected
		assertThatThrownBy(() -> passwordService.updatePassword(request))
			.isInstanceOf(PasswordNotMatched.class)
			.hasMessage(messageSourceUtils.getMessage("error.passwordNotMatched"));
	}

	private PasswordUpdateServiceRequest updateCreate(Long id, String password,
		String newPassword, String passwordConfirm) {
		return PasswordUpdateServiceRequest.builder()
			.id(id)
			.password(password)
			.newPassword(newPassword)
			.passwordConfirm(passwordConfirm)
			.build();
	}
}