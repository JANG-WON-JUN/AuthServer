package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.dto.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.enums.RoleType;

@MyServiceTest
class RoleQueryServiceTest {

	@Autowired
	private RoleQueryService roleQueryService;

	@Autowired
	private MessageSourceUtils messageSourceUtils;

	@Autowired
	private RoleRepository roleRepository;

	@Test
	@DisplayName("권한 이름으로 권한 객체를 조회할 수 있다.")
	void findByRoleName() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		Role role = createRole(roleType, roleType.getRoleDesc());

		roleRepository.save(role);

		// when
		Role savedRole = roleQueryService.findByRoleType(roleType);

		// then
		assertThat(savedRole.getId()).isNotNull();
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 이름으로 권한 객체 조회 시 권한이 없으면 예외를 발생시킨다.")
	void findByRoleNameWithNoRole() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		// expected
		assertThatThrownBy(() -> roleQueryService.findByRoleType(roleType))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	@Test
	@DisplayName("권한 id로 권한 객체를 조회할 수 있다.")
	void findById() {
		// given
		Long roleId = 1L;
		RoleType roleType = ROLE_ANONYMOUS;

		Role role = createRole(roleType, roleType.getRoleDesc());

		roleRepository.save(role);

		// when
		Role savedRole = roleQueryService.findById(roleId);

		// then
		assertThat(savedRole.getId()).isEqualTo(roleId);
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 아이디로 권한 객체 조회 시 권한이 없으면 예외를 발생시킨다.")
	void findByIdWithNoRole() {
		// given
		Long roleId = 1L;

		// expected
		assertThatThrownBy(() -> roleQueryService.findById(roleId))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	private Role createRole(RoleType roleType, String roleDesc) {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}
}