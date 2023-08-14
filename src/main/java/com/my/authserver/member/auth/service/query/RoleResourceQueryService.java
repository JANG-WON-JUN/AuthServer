package com.my.authserver.member.auth.service.query;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleResourceNotFound;
import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.repository.RoleResourceRepository;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.RoleType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleResourceQueryService {

	private final MessageSourceUtils messageSourceUtils;
	private final RoleResourceRepository roleResourceRepository;

	public RoleResource findById(Long id) {
		return roleResourceRepository.findById(id)
			.orElseThrow(() -> new RoleResourceNotFound(messageSourceUtils.getMessage("error.noRoleResource")));
	}

	@Cacheable(cacheNames = "roleResourcesCacheStore", key = "#roleType")
	public List<RoleResource> findByRoleType(RoleType roleType) {
		List<RoleResource> roleResources = roleResourceRepository.findByRoleType(roleType);
		lazyLoading(roleResources);
		return roleResources;
	}

	public Page<RoleResource> findRoleResources(RoleResourceSearchCondition condition) {
		Page<RoleResource> page = roleResourceRepository.findRoleResourcesWithCondition(condition);
		List<RoleResource> roleResources = page.getContent();
		lazyLoading(roleResources);
		return page;
	}

	private void lazyLoading(List<RoleResource> roleResources) {
		roleResources.stream()
			.forEach(roleResource -> {
				roleResource.getRole().getRoleType();
				roleResource.getResource().getResourceName();
			});
	}
}
