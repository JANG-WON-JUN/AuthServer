package com.my.authserver.domain.entity.member;

import static com.my.authserver.common.utils.CommonUtils.*;
import static java.time.Duration.*;
import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.authserver.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PASSWORD_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "char(60)")
	private String encodedValue;

	private LocalDateTime lastModDateTime; // 최근 비밀번호 변경 일자

	private LocalDateTime expireDateTime; // 비밀번호가 변경되어야 하는 날짜

	private int loginFailCount;

	private LocalDateTime loginLockTimes;

	@Setter
	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private Member member;

	@Transient
	private Integer lockLimitMinutes;

	@Transient
	private Period changeCycle;

	@Transient
	private static final int MAX_LOGIN_FAIL_COUNT = 5;

	@Transient
	private String passwordString;

	@Builder
	private Password(String passwordString, LocalDateTime lastModDateTime, Integer lockLimitMinutes,
		Period changeCycle) {
		this.passwordString = passwordString;
		this.lastModDateTime = lastModDateTime;
		this.lockLimitMinutes = lockLimitMinutes;
		this.changeCycle = changeCycle;
		this.expireDateTime = lastModDateTime.plusMonths(changeCycle.getMonths());
	}

	public static Password create(String passwordString, LocalDateTime lastModDateTime,
		Integer lockLimitMinutes, Period changeCycle) {
		return Password.builder()
			.passwordString(passwordString)
			.lastModDateTime(lastModDateTime)
			.lockLimitMinutes(lockLimitMinutes)
			.changeCycle(changeCycle)
			.build();
	}

	public boolean shouldChangePassword(LocalDateTime current) {
		return current.isAfter(expireDateTime);
	}

	public void extendExpireDate() {
		this.expireDateTime = relativeMonthFromNow(changeCycle.getMonths());
	}

	public void encodePassword(PasswordEncoder passwordEncoder) {
		if (hasText(passwordString)) {
			encodedValue = passwordEncoder.encode(passwordString);
		}
	}

	public boolean isPossibleLoginCheck() {
		return loginFailCount < 5 && loginLockTimes == null;
	}

	public int addLoginFailCount() {
		return ++loginFailCount;
	}

	public boolean canLoginAt(LocalDateTime currentTime) {
		if (loginLockTimes == null) {
			return true;
		}
		return between(loginLockTimes, currentTime).getSeconds() > 0
			&& loginFailCount >= MAX_LOGIN_FAIL_COUNT;
	}

	public boolean lock() {
		if (loginFailCount < MAX_LOGIN_FAIL_COUNT) {
			return false;
		}

		loginLockTimes = relativeMinuteFromNow(lockLimitMinutes);
		return true;
	}

	public void loginSuccess() {
		loginFailCount = 0;
		loginLockTimes = null;
	}

	public boolean shouldLocked() {
		return loginFailCount >= MAX_LOGIN_FAIL_COUNT;
	}

	public void changePassword(PasswordEncoder passwordEncoder, String password) {
		this.passwordString = password;
		encodePassword(passwordEncoder);
	}
}
