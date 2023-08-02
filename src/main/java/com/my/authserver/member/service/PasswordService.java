package com.my.authserver.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
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

	public Long createPassword(PasswordCreateServiceRequest request) {
		Assert.hasText(request.getPassword(), messageSourceUtils.getMessage("field.required.password"));
		Assert.notNull(request.getLastModDateTime(), messageSourceUtils.getMessage("field.required.lastModDateTime"));
		Assert.notNull(request.getLockLimitMinutes(), messageSourceUtils.getMessage("field.required.lockLimitMinutes"));
		Assert.notNull(request.getChangeCycle(), messageSourceUtils.getMessage("field.required.changeCycle"));

		Password password = request.toEntity();
		Password savedPassword = passwordRepository.save(password);

		return savedPassword.getId();
	}

	public Long updatePassword(PasswordUpdateServiceRequest request) {
		Assert.hasText(request.getPassword(), messageSourceUtils.getMessage("field.required.password"));

		if (notMatched(request.getPassword(), request.getPasswordConfirm())) {
			throw new PasswordNotValid(messageSourceUtils.getMessage("error.notValidPassword"));
		}
		
		Password savedPassword = passwordQueryService.findById(request.getId());

		savedPassword.changePassword(request.getPassword());

		return savedPassword.getId();
	}

	private boolean notMatched(String password, String passwordConfirm) {
		return !password.equals(passwordConfirm);
	}
}
