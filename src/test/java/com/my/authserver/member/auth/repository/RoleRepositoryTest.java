package com.my.authserver.member.auth.repository;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.my.authserver.annotation.MyDataJpaTest;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.member.web.request.RoleSearchCondition;

@MyDataJpaTest
class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	@DisplayName("권한 이름으로 권한 객체를 조회할 수 있다.")
	void findByRoleName() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		Role role1 = createRole(roleType, roleType.getRoleDesc());

		roleRepository.save(role1);

		// when
		Role savedRole = roleRepository.findByRoleType(roleType).get();

		// then
		assertThat(savedRole.getId()).isNotNull();
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("권한 이름으로 권한 객체 조회 시 권한이 없으면 비어있는 optional을 반환한다.")
	void findByRoleNameWithNoRole() {
		// given
		RoleType roleType = ROLE_ANONYMOUS;

		// expected
		Optional<Role> result = roleRepository.findByRoleType(roleType);

		// then
		assertThat(result).isEqualTo(Optional.empty());
	}

	@Test
	@DisplayName("조회 조건을 적용하여 권한 목록을 조회한다.")
	void findRolesWithCondition() {
		// given
		RoleType roleType1 = ROLE_ANONYMOUS;
		RoleType roleType2 = ROLE_MEMBER;
		RoleType roleType3 = ROLE_ADMIN;

		Role role1 = createRole(roleType1, roleType1.getRoleDesc());
		Role role2 = createRole(roleType2, roleType2.getRoleDesc());
		Role role3 = createRole(roleType3, roleType3.getRoleDesc());

		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);

		RoleSearchCondition condition = createRoleSearchCondition(0, null);

		// when
		Page<Role> page = roleRepository.findRolesWithCondition(condition);

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

		Role role1 = createRole(roleType1, roleType1.getRoleDesc());
		Role role2 = createRole(roleType2, roleType2.getRoleDesc());
		Role role3 = createRole(roleType3, roleType3.getRoleDesc());

		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);

		RoleSearchCondition condition = createRoleSearchCondition(0, "회원");

		// when
		Page<Role> page = roleRepository.findRolesWithCondition(condition);

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
	@DisplayName("권한이 조회되지 않는 페이지 번호를 가지고 권한 목록을 조회 시 비어있는 리스트롤 반환한다.")
	void findRolesWithConditionWithInvalidPage() {
		// given
		RoleSearchCondition condition = createRoleSearchCondition(0, null);

		// when
		Page<Role> page = roleRepository.findRolesWithCondition(condition);

		// then
		List<Role> savedRoles = page.getContent();

		assertThat(savedRoles).isEmpty();
	}

	private Role createRole(RoleType roleType, String roleDesc) {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}

	private RoleSearchCondition createRoleSearchCondition(int page, String keyword) {
		return RoleSearchCondition.builder()
			.page(page)
			.keyword(keyword)
			.build();
	}
}