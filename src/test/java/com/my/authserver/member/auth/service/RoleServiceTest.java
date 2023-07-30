package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.dto.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.request.RoleCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

@MyServiceTest
class RoleServiceTest {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleQueryService roleQueryService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MessageSourceUtils messageSourceUtils;

	@Test
	@DisplayName("권한 타입과 권한 설명을 받아 권한을 생성한다.")
	void createRole() {
		// given
		RoleType roleType = RoleType.ROLE_ANONYMOUS;
		RoleCreateServiceRequest request = createRequest(roleType, roleType.getRoleDesc());

		// when
		roleService.createRole(request);

		// then
		Role savedRole = roleQueryService.findByRoleType(roleType);

		assertThat(savedRole.getId()).isNotNull();
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 생성 시 권한 타입이 없으면 예외를 발생시킨다.")
	void createRoleWithNoRoleType() {
		// given
		RoleCreateServiceRequest request = createRequest(null, "권한 설명");

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleType"));
	}

	@Test
	@DisplayName("권한 생성 시 권한 설명이 없으면 예외를 발생시킨다.")
	void createRoleWithNoRoleDesc() {
		// given
		RoleCreateServiceRequest request = createRequest(ROLE_ADMIN, null);

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleDesc"));
	}

	@Test
	@DisplayName("권한 생성 시 권한 설명이 빈값이거나 공백이면 예외를 발생시킨다.")
	void createRoleWithWhiteSpaceRoleDesc() {
		// given
		RoleCreateServiceRequest request = createRequest(ROLE_ADMIN, " ");

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleDesc"));
	}

	@Test
	@DisplayName("권한의 id로 권한을 삭제한다.")
	void deleteRole() {
		// given
		RoleType roleType = ROLE_ADMIN;
		RoleCreateServiceRequest request = createRequest(roleType, roleType.getRoleDesc());

		Long savedRoleId = roleService.createRole(request);

		// when
		roleService.deleteRole(savedRoleId);

		// then
		List<Role> roles = roleRepository.findAll();

		assertThat(roles).isEmpty();
	}

	@Test
	@DisplayName("존재하지 않는 권한의 id로 삭제 시 예외가 발생한다.")
	void deleteRoleWithNoRoleId() {
		// given
		Long notExistRoleId = 1L;

		// expected
		assertThatThrownBy(() -> roleService.deleteRole(notExistRoleId))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	private RoleCreateServiceRequest createRequest(RoleType roleType, String roleDesc) {
		return RoleCreateServiceRequest.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}
}