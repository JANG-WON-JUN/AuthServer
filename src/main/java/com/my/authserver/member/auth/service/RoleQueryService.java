package com.my.authserver.member.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.enums.RoleType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleQueryService {

	private final RoleRepository roleRepository;

	public Role findByRoleType(RoleType roleType) {
		return roleRepository.findByRoleType(roleType);
	}
}
