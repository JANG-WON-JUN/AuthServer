package com.my.authserver.member.enums;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTypeTest {

	@Test
	@DisplayName("권한 이름으로 권한 타입을 검색하여 일치하는 권한을 조회한다.")
	void findRole() {
		// given
		String roleName1 = "ROLE_ANONYMOUS";
		String roleName2 = "ROLE_MEMBER";
		String roleName3 = "ROLE_ADMIN";

		// when
		RoleType roleType1 = RoleType.findRole(roleName1);
		RoleType roleType2 = RoleType.findRole(roleName2);
		RoleType roleType3 = RoleType.findRole(roleName3);

		// then
		assertThat(roleType1).isEqualByComparingTo(ROLE_ANONYMOUS);
		assertThat(roleType2).isEqualByComparingTo(ROLE_MEMBER);
		assertThat(roleType3).isEqualByComparingTo(ROLE_ADMIN);
	}

	@Test
	@DisplayName("권한 이름으로 권한 타입을 검색 시 일치하는 권한이 없으면 기본값으로 비회원 권한을 반환한다.")
	void findRoleWithNotValidRoleName() {
		// given
		String roleName = "not valid roleName";

		// when
		RoleType roleType = RoleType.findRole(roleName);

		// then
		assertThat(roleType).isEqualByComparingTo(ROLE_ANONYMOUS);
	}

	@Test
	@DisplayName("관리자 권한은 회원과 비회원보다 우선순위가 높다.")
	void adminHasHigherPriorityThan() {
		// given
		RoleType adminRoleType = ROLE_ADMIN;
		RoleType memberRoleType = ROLE_MEMBER;
		RoleType anonymousRoleType = ROLE_ANONYMOUS;

		// when
		boolean result1 = adminRoleType.hasHigherPriorityThan(memberRoleType);
		boolean result2 = memberRoleType.hasHigherPriorityThan(anonymousRoleType);

		// then
		assertThat(result1).isTrue();
		assertThat(result2).isTrue();
	}

	@Test
	@DisplayName("회원 권한은 관리자보다는 우선순위가 낮고, 비회원보다는 높다.")
	void memberHasHigherPriorityThan() {
		// given
		RoleType adminRoleType = ROLE_ADMIN;
		RoleType memberRoleType = ROLE_MEMBER;
		RoleType anonymousRoleType = ROLE_ANONYMOUS;

		// when
		boolean result1 = memberRoleType.hasHigherPriorityThan(adminRoleType);
		boolean result2 = memberRoleType.hasHigherPriorityThan(anonymousRoleType);

		// then
		assertThat(result1).isFalse();
		assertThat(result2).isTrue();
	}

	@Test
	@DisplayName("비회원 권한은 다른 권한보다 우선순위가 낮다.")
	void anonymousHasHigherPriorityThan() {
		// given
		RoleType adminRoleType = ROLE_ADMIN;
		RoleType memberRoleType = ROLE_MEMBER;
		RoleType anonymousRoleType = ROLE_ANONYMOUS;

		// when
		boolean result1 = anonymousRoleType.hasHigherPriorityThan(adminRoleType);
		boolean result2 = anonymousRoleType.hasHigherPriorityThan(memberRoleType);

		// then
		assertThat(result1).isFalse();
		assertThat(result2).isFalse();
	}

	@Test
	@DisplayName("동일한 권한의 우선순의를 비교하면 거짓을 반환한다.")
	void hasHigherPriorityThanWithSameRoleType() {
		// given
		RoleType roleType = ROLE_ADMIN;

		// when
		boolean result = roleType.hasHigherPriorityThan(roleType);

		// then
		assertThat(result).isFalse();
	}
}