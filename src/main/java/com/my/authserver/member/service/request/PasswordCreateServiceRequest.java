package com.my.authserver.member.service.request;

import java.time.LocalDateTime;
import java.time.Period;

import com.my.authserver.domain.entity.member.Password;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordCreateServiceRequest {

	private String password;
	private LocalDateTime lastModDateTime;
	private Integer lockLimitMinutes;
	private Period changeCycle;

	@Builder
	private PasswordCreateServiceRequest(String password, LocalDateTime lastModDateTime,
		Integer lockLimitMinutes, Period changeCycle) {
		this.password = password;
		this.lastModDateTime = lastModDateTime;
		this.lockLimitMinutes = lockLimitMinutes;
		this.changeCycle = changeCycle;
	}

	public static PasswordCreateServiceRequest create(String password, LocalDateTime lastModDateTime,
		Integer lockLimitMinutes, Period changeCycle) {
		return PasswordCreateServiceRequest.builder()
			.password(password)
			.lastModDateTime(lastModDateTime)
			.lockLimitMinutes(lockLimitMinutes)
			.changeCycle(changeCycle)
			.build();
	}

	public Password toEntity() {
		return Password.builder()
			.passwordString(password)
			.lockLimitMinutes(lockLimitMinutes)
			.lastModDateTime(lastModDateTime)
			.changeCycle(changeCycle)
			.build();
	}
}
