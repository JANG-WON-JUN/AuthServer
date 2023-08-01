package com.my.authserver.member.auth.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleHierarchyNotFound;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.repository.RoleHierarchyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleHierarchyQueryService {

	private final MessageSourceUtils messageSourceUtils;
	private final RoleHierarchyRepository roleHierarchyRepository;

	public List<RoleHierarchy> findRoleHierarchies() {
		return roleHierarchyRepository.findAll();
	}

	public RoleHierarchy findById(Long id) {
		return roleHierarchyRepository.findById(id).orElseThrow(()
			-> new RoleHierarchyNotFound(messageSourceUtils.getMessage("error.noRoleHierarchy")));
	}
}
