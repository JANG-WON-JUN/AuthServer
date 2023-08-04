package com.my.authserver.member.web.request;

import com.my.authserver.member.service.request.PasswordUpdateServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordUpdateRequest {

	@NotNull(message = "{field.required.id}")
	private Long id;
	@NotBlank(message = "{field.required.password}")
	private String password;
	@NotBlank(message = "{field.required.newPassword}")
	private String newPassword;

	public PasswordUpdateServiceRequest toServiceRequest() {
		return PasswordUpdateServiceRequest.builder()
			.id(id)
			.password(password)
			.newPassword(newPassword)
			.build();
	}
}
