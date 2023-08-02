package com.my.authserver.domain.entity.member;

import static com.my.authserver.common.utils.CommonUtils.*;
import static java.time.Duration.*;

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
	private String password;

	private LocalDateTime lastModDateTime; // 최근 비밀번호 변경 일자

	private LocalDateTime expireDateTime; // 비밀번호가 변경되어야 하는 날짜

	private int loginFailCount;

	private LocalDateTime loginLockTime;

	private boolean isLocked;

	@Setter
	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private Member member;

	@Transient
	private Integer lockLimitMinute;

	@Transient
	private Period changeCycle;

	@Transient
	private static final int MAX_LOGIN_FAIL_COUNT = 5;

	@Builder
	private Password(String password, LocalDateTime lastModDateTime, Integer lockLimitMinute, Period changeCycle) {
		this.password = password;
		this.lastModDateTime = lastModDateTime;
		this.lockLimitMinute = lockLimitMinute;
		this.changeCycle = changeCycle;
		this.expireDateTime = lastModDateTime.plusMonths(changeCycle.getMonths());
	}

	public static Password create(String password, LocalDateTime lastModDateTime,
		Integer lockLimitMinute, Period changeCycle) {
		return Password.builder()
			.password(password)
			.lastModDateTime(lastModDateTime)
			.lockLimitMinute(lockLimitMinute)
			.changeCycle(changeCycle)
			.build();
	}

	// 비밀번호 변경되어야 할 날짜

	public boolean shouldChangePassword(LocalDateTime current) {
		return current.isAfter(expireDateTime);
	}

	public void extendExpireDate() {
		this.expireDateTime = relativeMonthFromNow(changeCycle.getMonths());
	}

	public void encodePassword(PasswordEncoder passwordEncoder) {
		password = passwordEncoder.encode(password);
	}

	public boolean isPossibleLoginCheck() {
		return loginFailCount < 5 && loginLockTime == null;
	}

	public int addLoginFailCount() {
		return ++loginFailCount;
	}

	public boolean canLoginAt(LocalDateTime currentTime) {
		if (loginLockTime == null) {
			return true;
		}
		return between(loginLockTime, currentTime).getSeconds() > 0
			&& loginFailCount >= MAX_LOGIN_FAIL_COUNT;
	}

	public boolean lock() {
		if (loginFailCount < MAX_LOGIN_FAIL_COUNT) {
			return false;
		}

		loginLockTime = relativeMinuteFromNow(lockLimitMinute);
		return true;
	}

	public void loginSuccess() {
		loginFailCount = 0;
		loginLockTime = null;
	}

	public boolean shouldLocked() {
		return loginFailCount >= MAX_LOGIN_FAIL_COUNT;
	}

}
