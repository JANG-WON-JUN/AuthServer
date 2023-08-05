package com.my.authserver.member.auth.service.query;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import com.my.authserver.common.web.exception.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.web.request.RoleSearchCondition;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.support.service.ServiceTestSupport;

class RoleQueryServiceTest extends ServiceTestSupport {

	@Test
	@DisplayName("권한 이름으로 권한 객체를 조회할 수 있다.")
	void findByRoleName() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		saveRole(roleType, roleType.getRoleDesc());

		// when
		Role savedRole = roleQueryService.findByRoleType(roleType);

		// then
		assertThat(savedRole.getId()).isNotNull();
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 이름으로 권한 객체 조회 시 존재하지 않는 권한을 조회할 수 없다.")
	void findByRoleNameWithNoRole() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		// expected
		assertThatThrownBy(() -> roleQueryService.findByRoleType(roleType))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	@Test
	@DisplayName("권한 아이디로 권한 객체를 조회할 수 있다.")
	void findById() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		Long savedRoleId = saveRole(roleType, roleType.getRoleDesc());

		// when
		Role savedRole = roleQueryService.findById(savedRoleId);

		// then
		assertThat(savedRole.getId()).isEqualTo(savedRoleId);
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 아이디로 권한 객체 조회 시 존재하지 않는 권한을 조회할 수 없다.")
	void findByIdWithNoRole() {
		// given
		Long roleId = 1L;

		// expected
		assertThatThrownBy(() -> roleQueryService.findById(roleId))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	@Test
	@DisplayName("조회 조건을 적용하여 권한 목록을 조회한다.")
	void findRolesWithCondition() {
		// given
		RoleType roleType1 = ROLE_ANONYMOUS;
		RoleType roleType2 = ROLE_MEMBER;
		RoleType roleType3 = ROLE_ADMIN;

		saveRoles(List.of(roleType1, roleType2, roleType3));

		RoleSearchCondition condition = createRoleSearchCondition(0, null);

		// when
		Page<Role> page = roleQueryService.findRolesWithCondition(condition);

		// then
		List<Role> savedRoles = page.getContent();

		assertThat(savedRoles).hasSize(3)
			.extracting("roleType", "roleDesc")
			.containsExactlyInAnyOrder(
				tuple(roleType1, roleType1.getRoleDesc()),
				tuple(roleType2, roleType2.getRoleDesc()),
				tuple(roleType3, roleType3.getRoleDesc())
			);
	}

	@Test
	@DisplayName("권한 설명을 조회 조건으로 받아 조건을 만족하는 권한 목록을 조회한다.")
	void findRolesWithConditionWithKeyword() {
		// given
		RoleType roleType1 = ROLE_ANONYMOUS;
		RoleType roleType2 = ROLE_MEMBER;
		RoleType roleType3 = ROLE_ADMIN;

		saveRoles(List.of(roleType1, roleType2, roleType3));

		RoleSearchCondition condition = createRoleSearchCondition(0, "회원");

		// when
		Page<Role> page = roleQueryService.findRolesWithCondition(condition);

		// then
		List<Role> savedRoles = page.getContent();

		assertThat(savedRoles).hasSize(2)
			.extracting("roleType", "roleDesc")
			.containsExactlyInAnyOrder(
				tuple(roleType1, roleType1.getRoleDesc()),
				tuple(roleType2, roleType2.getRoleDesc())
			);
	}

	@Test
	@DisplayName("권한이 존재하면 참을 반환한다.")
	void exists() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		saveRole(roleType, roleType.getRoleDesc());

		// when
		boolean exists = roleQueryService.exists(roleType);

		// then
		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("권한이 존재하지 않으면 거짓을 반환한다.")
	void existsWithNoRole() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		// when
		boolean exists = roleQueryService.exists(roleType);

		// then
		assertThat(exists).isFalse();
	}

	private Role createRole(RoleType roleType, String roleDesc) {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}

	private Long saveRole(RoleType roleType, String roleDesc) {
		Role role = createRole(roleType, roleDesc);
		return roleRepository.save(role).getId();
	}

	private void saveRoles(List<RoleType> roleTypes) {
		roleTypes.stream()
			.map(roleType -> createRole(roleType, roleType.getRoleDesc()))
			.forEach(role -> roleRepository.save(role));
	}

	private RoleSearchCondition createRoleSearchCondition(int page, String keyword) {
		return RoleSearchCondition.builder()
			.page(page)
			.keyword(keyword)
			.build();
	}
}