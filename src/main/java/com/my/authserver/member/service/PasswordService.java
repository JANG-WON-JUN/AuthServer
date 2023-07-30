package com.my.authserver.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.repository.PasswordRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordRepository passwordRepository;

	public void createPassword(Password password) {
		password.extendBeChangedDate();
		passwordRepository.save(password);
	}
}
