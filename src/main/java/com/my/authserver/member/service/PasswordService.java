package com.my.authserver.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.PasswordNotMatched;
import com.my.authserver.common.web.exception.PasswordNotValid;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.repository.PasswordRepository;
import com.my.authserver.member.service.query.PasswordQueryService;
import com.my.authserver.member.service.request.PasswordCreateServiceRequest;
import com.my.authserver.member.service.request.PasswordUpdateServiceRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordQueryService passwordQueryService;
	private final MessageSourceUtils messageSourceUtils;
	private final PasswordRepository passwordRepository;
	private final PasswordEncoder passwordEncoder;

	public Long createPassword(PasswordCreateServiceRequest request) {
		Assert.hasText(request.getPassword(), messageSourceUtils.getMessage("field.required.password"));
		Assert.notNull(request.getLastModDateTime(), messageSourceUtils.getMessage("field.required.lastModDateTime"));
		Assert.notNull(request.getLockLimitMinutes(), messageSourceUtils.getMessage("field.required.lockLimitMinutes"));
		Assert.notNull(request.getChangeCycle(), messageSourceUtils.getMessage("field.required.changeCycle"));

		Password savedPassword = passwordRepository.save(request.toEntity());

		savedPassword.encodePassword(passwordEncoder);

		return savedPassword.getId();
	}

	public Long updatePassword(PasswordUpdateServiceRequest request) {
		Password savedPassword = passwordQueryService.findById(request.getId());

		updateValidCheck(request, savedPassword);

		savedPassword.changePassword(passwordEncoder, request.getNewPassword());

		return savedPassword.getId();
	}

	private void updateValidCheck(PasswordUpdateServiceRequest request, Password savedPassword) {
		String oldPassword = request.getPassword();
		String newPassword = request.getNewPassword();
		String passwordConfirm = request.getPasswordConfirm();

		Assert.hasText(oldPassword, messageSourceUtils.getMessage("field.required.password"));
		Assert.hasText(newPassword, messageSourceUtils.getMessage("field.required.newPassword"));
		Assert.hasText(passwordConfirm, messageSourceUtils.getMessage("field.required.passwordConfirm"));

		if (notMatched(newPassword, passwordConfirm)) {
			throw new PasswordNotValid(messageSourceUtils.getMessage("error.notValidPassword"));
		}

		if (!passwordEncoder.matches(oldPassword, savedPassword.getPassword())) {
			throw new PasswordNotMatched(messageSourceUtils.getMessage("error.passwordNotMatched"));
		}
	}

	private boolean notMatched(String password, String passwordConfirm) {
		return !password.equals(passwordConfirm);
	}
}
