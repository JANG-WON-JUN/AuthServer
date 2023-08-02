package com.my.authserver.domain.entity.member;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

	@Test
	@DisplayName("비밀번호 변경날짜가 지나지 않으면 비밀번호를 변경하지 않아도 된다.")
	void shouldNotChangePassword() {
		// given
		LocalDateTime lastModDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
		LocalDateTime currentDateTime = LocalDateTime.of(2023, Month.MARCH, 1, 0, 0, 0);
		Password password = createPassword(lastModDateTime, Period.ofMonths(3));

		// when
		boolean shouldChangePassword = password.shouldChangePassword(currentDateTime);

		// then
		assertThat(shouldChangePassword).isFalse();
	}

	@Test
	@DisplayName("비밀번호 변경날짜가 지나면 비밀번호를 변경해야 한다.")
	void shouldChangePassword() {
		// given
		LocalDateTime lastModDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
		LocalDateTime currentDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0);
		Password password = createPassword(lastModDateTime, Period.ofMonths(1));

		// when
		boolean shouldChangePassword = password.shouldChangePassword(currentDateTime);

		// then
		assertThat(shouldChangePassword).isTrue();
	}

	@Test
	@DisplayName("비밀번호 변경날짜가 지났을 때 비밀번호를 변경하지 않고 연장할 수 있다.")
	void possibleExtendBeChangedDate() {
		// given
		LocalDateTime lastModDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
		LocalDateTime currentDateTime = LocalDateTime.of(2023, 3, 1, 0, 0, 0);
		Password password = createPassword(lastModDateTime, Period.ofMonths(1));

		password.shouldChangePassword(currentDateTime);
		password.extendExpireDate();

		// when
		boolean shouldChangePassword = password.shouldChangePassword(currentDateTime);

		// then
		assertThat(shouldChangePassword).isFalse();
	}

	@Test
	@DisplayName("로그인 실패 시 로그인 실패 횟수가 1 증가한다.")
	void loginFailCount() {
		// given
		Password password = createPassword(LocalDateTime.MIN, Period.ofMonths(1));

		// when
		password.addLoginFailCount();

		// then
		int loginFailCount = (int)getField(password, "loginFailCount");

		assertThat(loginFailCount).isEqualTo(1L);
	}

	@Test
	@DisplayName("로그인 5회 실패 시 대기 시간이 지나지 않으면 로그인 할 수 없다.")
	void passwordLock() {
		// given
		Password password = createPassword(LocalDateTime.MIN, Period.ofMonths(1));
		setField(password, "loginFailCount", 5);

		password.lock();

		// when
		boolean canLogin = password.canLoginAt(LocalDateTime.now());

		// then
		assertThat(canLogin).isFalse();
	}

	@Test
	@DisplayName(" 로그인 5회 실패 시, 대기 시간이 지나면 다시 로그인할 수 있다.")
	void releasePasswordLock() {
		// given
		Password password = createPassword(LocalDateTime.MIN, Period.ofMonths(1));
		setField(password, "loginFailCount", 5);

		password.lock();

		// when
		boolean canLogin = password.canLoginAt(LocalDateTime.MAX);

		// then
		assertThat(canLogin).isTrue();
	}

	@Test
	@DisplayName(" 로그인 5회 실패 시, 1분이 경과하면 다시 로그인할 수 있다.")
	void releasePasswordLock2() {
		// given
		Password password = createPassword(LocalDateTime.MIN, Period.ofMonths(1));
		setField(password, "loginFailCount", 5);

		password.lock();

		// when
		boolean isReleasableLoginLock = true;

		// then
		assertThat(isReleasableLoginLock).isTrue();
	}

	@Test
	@DisplayName("로그인 5회 실패 시 1분이 지나지 않으면 로그인할 수 없다.")
	void notReleasePasswordLock() {
		// given
		Password password = createPassword(LocalDateTime.MIN, Period.ofMonths(1));
		setField(password, "loginFailCount", 1);

		// when
		boolean isLocked = password.lock();

		// then
		assertThat(isLocked).isFalse();
	}

	private Password createPassword(LocalDateTime lastModDateTime, Period changeCycle) {
		return Password.create("", lastModDateTime, 0, changeCycle);
	}
}