package com.my.authserver.member.auth.service;

import static com.my.authserver.domain.entity.member.auth.RoleResource.*;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.repository.RoleResourceRepository;
import com.my.authserver.member.auth.service.query.ResourceQueryService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.service.query.RoleResourceQueryService;
import com.my.authserver.member.auth.service.request.RoleResourceCreateServiceRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleResourceService {

	private final RoleQueryService roleQueryService;
	private final ResourceQueryService resourceQueryService;
	private final RoleResourceQueryService roleResourceQueryService;
	private final MessageSourceUtils messageSourceUtils;
	private final RoleResourceRepository roleResourceRepository;

	public Long createRoleResource(RoleResourceCreateServiceRequest request) {
		Assert.notNull(request.getRoleId(), messageSourceUtils.getMessage("field.required.role"));
		Assert.notNull(request.getResourceId(), messageSourceUtils.getMessage("field.required.resource"));

		Role savedRole = roleQueryService.findById(request.getRoleId());
		Resource savedResource = resourceQueryService.findById(request.getResourceId());
		RoleResource savedRoleResource = roleResourceRepository.save(create(savedRole, savedResource));

		return savedRoleResource.getId();
	}

	@CacheEvict(cacheNames = "roleResourcesCacheStore", allEntries = true)
	public void deleteRoleResource(Long id) {
		RoleResource savedRoleResource = roleResourceQueryService.findById(id);
		roleResourceRepository.deleteById(savedRoleResource.getId());
	}

}
