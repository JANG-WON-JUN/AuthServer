package com.my.authserver.member.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.PasswordNotFound;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.repository.PasswordRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PasswordQueryService {

	private final MessageSourceUtils messageSourceUtils;
	private final PasswordRepository passwordRepository;

	public Password findById(Long id) {
		return passwordRepository.findById(id)
			.orElseThrow(() -> new PasswordNotFound(messageSourceUtils.getMessage("error.noPassword")));
	}

}
