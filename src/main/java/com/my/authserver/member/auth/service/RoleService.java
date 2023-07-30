package com.my.authserver.member.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.dto.RoleAlreadyExists;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.request.RoleCreateServiceRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

	private final MessageSourceUtils messageSourceUtils;
	private final RoleQueryService roleQueryService;
	private final RoleRepository roleRepository;

	public Long createRole(RoleCreateServiceRequest request) {
		Assert.notNull(request.getRoleType(), messageSourceUtils.getMessage("error.noRoleType"));
		Assert.hasText(request.getRoleDesc(), messageSourceUtils.getMessage("error.noRoleDesc"));

		if (roleQueryService.exists(request.getRoleType())) {
			throw new RoleAlreadyExists(messageSourceUtils.getMessage("error.roleAlreadyExist"));
		}

		Role savedRole = roleRepository.save(request.toEntity());

		return savedRole.getId();
	}

	public void deleteRole(Long id) {
		Long savedRoleId = roleQueryService.findById(id).getId();
		roleRepository.deleteById(savedRoleId);
	}

}
