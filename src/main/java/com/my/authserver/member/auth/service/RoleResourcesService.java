package com.my.authserver.member.auth.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.domain.entity.member.auth.RoleResources;
import com.my.authserver.member.auth.repository.RoleResourcesRepository;
import com.my.authserver.member.enums.Roles;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleResourcesService {

	private final RoleResourcesRepository roleResourcesRepository;

	public void createRoleResources(RoleResources roleResources) {
		roleResourcesRepository.save(roleResources);
	}

	@Cacheable("roleResourcesCacheStore")
	public List<RoleResources> findByRoleName(Roles role) {
		return roleResourcesRepository.findByIdRoleRoleName(role);
	}
}
