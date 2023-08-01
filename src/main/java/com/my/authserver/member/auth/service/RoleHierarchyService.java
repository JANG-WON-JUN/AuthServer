package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.RoleType.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleHierarchyNotValid;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.repository.RoleHierarchyRepository;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.service.request.RoleHierarchyCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleHierarchyService {

	private final RoleQueryService roleQueryService;
	private final MessageSourceUtils messageSourceUtils;
	private final RoleHierarchyQueryService roleHierarchyQueryService;
	private final RoleHierarchyRepository roleHierarchyRepository;

	public RoleHierarchy createRoleHierarchy(RoleHierarchyCreateServiceRequest request) throws RoleHierarchyNotValid {
		RoleType parent = request.getParent();
		RoleType child = request.getChild();

		// 최상위 권한은 부모 권한을 null로 입력받을 수 있다.
		Assert.notNull(child, messageSourceUtils.getMessage("field.required.childRoleType"));

		if (isNotValidHierarchy(parent, child)) {
			throw new RoleHierarchyNotValid(messageSourceUtils.getMessage("error.notValidRoleHierarchy"));
		}

		Role parentRole = parent == null ? null : roleQueryService.findByRoleType(parent);
		Role childRole = roleQueryService.findByRoleType(child);
		RoleHierarchy roleHierarchy = RoleHierarchy.create(parentRole, childRole);
		RoleHierarchy savedRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

		return savedRoleHierarchy;
	}

	private boolean isNotValidHierarchy(RoleType parent, RoleType child) {
		if (parent == null && child != getTopPriorityRole()) {
			return true;
		}

		if (parent == null && child == getTopPriorityRole()) {
			return false;
		}

		if (!parent.hasHigherPriorityThan(child)) {
			return true;
		}

		return false;
	}

	public void deleteRoleHierarchy(Long id) {
		Long savedRoleHierarchyId = roleHierarchyQueryService.findById(id).getId();
		roleHierarchyRepository.deleteById(savedRoleHierarchyId);
	}

}
