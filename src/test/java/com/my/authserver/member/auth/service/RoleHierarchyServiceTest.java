package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleHierarchyNotFound;
import com.my.authserver.common.web.exception.RoleHierarchyNotValid;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.repository.RoleHierarchyRepository;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.service.request.RoleHierarchyCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

@MyServiceTest
class RoleHierarchyServiceTest {

	@Autowired
	private RoleHierarchyService roleHierarchyService;

	@Autowired
	private RoleHierarchyQueryService roleHierarchyQueryService;

	@Autowired
	private MessageSourceUtils messageSourceUtils;

	@Autowired
	private RoleHierarchyRepository roleHierarchyRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Test
	@DisplayName("관리자 권한과 회원 권한을 입력받아 권한 계층을 생성한다.")
	void createRoleHierarchy() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(ROLE_ADMIN, ROLE_MEMBER);

		// when
		Long savedRoleHierarchyId = roleHierarchyService.createRoleHierarchy(request);

		// then
		RoleHierarchy savedRoleHierarchy = roleHierarchyQueryService.findById(savedRoleHierarchyId);
		assertThat(savedRoleHierarchy.getId()).isNotNull();
		assertThat(savedRoleHierarchy.getParent().getRoleType()).isEqualByComparingTo(ROLE_ADMIN);
		assertThat(savedRoleHierarchy.getChild().getRoleType()).isEqualByComparingTo(ROLE_MEMBER);
	}

	@Test
	@DisplayName("관리자는 부모를 null로 입력하여 저장할 수 있다.")
	void createRoleHierarchyWithTopPriority() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(null, ROLE_ADMIN);

		// when
		Long savedRoleHierarchyId = roleHierarchyService.createRoleHierarchy(request);

		// then
		RoleHierarchy savedRoleHierarchy = roleHierarchyQueryService.findById(savedRoleHierarchyId);
		assertThat(savedRoleHierarchy.getId()).isNotNull();
		assertThat(savedRoleHierarchy.getParent()).isNull();
		assertThat(savedRoleHierarchy.getChild().getRoleType()).isEqualByComparingTo(ROLE_ADMIN);
	}

	@Test
	@DisplayName("관리자를 제외한 나머지 권한은 부모를 null로 입력하여 저장할 수 없다.")
	void createRoleHierarchyWithOthersPriority() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(null, ROLE_MEMBER);

		// expected
		assertThatThrownBy(() -> roleHierarchyService.createRoleHierarchy(request))
			.isInstanceOf(RoleHierarchyNotValid.class)
			.hasMessage(messageSourceUtils.getMessage("error.notValidRoleHierarchy"));
	}

	@Test
	@DisplayName("권한 우선순위가 낮은 권한을 부모 권한으로 입력하면 예외가 발생한다.")
	void createRoleHierarchyWithNoValidHierarchy() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(ROLE_ANONYMOUS, ROLE_ADMIN);

		// expected
		assertThatThrownBy(() -> roleHierarchyService.createRoleHierarchy(request))
			.isInstanceOf(RoleHierarchyNotValid.class)
			.hasMessage(messageSourceUtils.getMessage("error.notValidRoleHierarchy"));
	}

	@Test
	@DisplayName("동일한 권한을 입력받아 권한 계층 생성 시 예외가 발생한다.")
	void createRoleHierarchyWithSameRole() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(ROLE_ADMIN, ROLE_ADMIN);

		// expected
		assertThatThrownBy(() -> roleHierarchyService.createRoleHierarchy(request))
			.isInstanceOf(RoleHierarchyNotValid.class)
			.hasMessage(messageSourceUtils.getMessage("error.notValidRoleHierarchy"));
	}

	@Test
	@DisplayName("권한 계층 생성 시에 자식 권한은 필수 입력이다.")
	void createRoleHierarchyWithNoRoleHierarchy() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(ROLE_ADMIN, null);

		// expected
		assertThatThrownBy(() -> roleHierarchyService.createRoleHierarchy(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.childRoleType"));
	}

	@Test
	@DisplayName("권한 계층 id로 삭제할 수 있다.")
	void deleteRoleHierarchy() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request = createRoleHierarchy(null, ROLE_ADMIN);

		Long savedRoleHierarchyId = roleHierarchyService.createRoleHierarchy(request);

		// when
		roleHierarchyService.deleteRoleHierarchy(savedRoleHierarchyId);

		// then
		assertThat(roleHierarchyRepository.count()).isZero();
	}

	@Test
	@DisplayName("권한 계층 삭제 시 이미 존재하지 않는 권한 계층인 경우 예외가 발생한다.")
	void deleteRoleHierarchyWithInvalidId() {
		// given
		saveRoles();

		// expected
		assertThatThrownBy(() -> roleHierarchyService.deleteRoleHierarchy(1000L))
			.isInstanceOf(RoleHierarchyNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleHierarchy"));
	}

	private RoleHierarchyCreateServiceRequest createRoleHierarchy(RoleType parent, RoleType child) {
		return RoleHierarchyCreateServiceRequest.builder()
			.parent(parent)
			.child(child)
			.build();
	}

	private void saveRoles() {
		Role role1 = createRole(ROLE_ANONYMOUS);
		Role role2 = createRole(ROLE_MEMBER);
		Role role3 = createRole(ROLE_ADMIN);

		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);
	}

	private Role createRole(RoleType roleType) {
		return Role.builder()
			.roleType(roleType)
			.roleDesc(roleType.getRoleDesc())
			.build();
	}
}