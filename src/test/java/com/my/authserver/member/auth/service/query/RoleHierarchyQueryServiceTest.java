package com.my.authserver.member.auth.service.query;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleHierarchyNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.entity.member.auth.RoleHierarchy;
import com.my.authserver.member.auth.repository.RoleHierarchyRepository;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.request.RoleHierarchyCreateServiceRequest;
import com.my.authserver.member.enums.RoleType;

@MyServiceTest
class RoleHierarchyQueryServiceTest {

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
	@DisplayName("권한 계층 아이디로 권한 계층 객체를 조회할 수 있다.")
	void findById() {
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
	@DisplayName("존재하지 않는 권한 계층 아이디로 권한 계층 객체를 조회 시 예외가 발생한다.")
	void findByIdWithNoId() {
		// given

		// expected
		assertThatThrownBy(() -> roleHierarchyQueryService.findById(1L))
			.isInstanceOf(RoleHierarchyNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRoleHierarchy"));
	}

	@Test
	@DisplayName("권한 계층을 전체 조회할 수 있다.")
	void findRoleHierarchies() {
		// given
		saveRoles();
		RoleHierarchyCreateServiceRequest request1 = createRoleHierarchy(null, ROLE_ADMIN);
		RoleHierarchyCreateServiceRequest request2 = createRoleHierarchy(ROLE_ADMIN, ROLE_MEMBER);
		RoleHierarchyCreateServiceRequest request3 = createRoleHierarchy(ROLE_MEMBER, ROLE_ANONYMOUS);

		roleHierarchyService.createRoleHierarchy(request1);
		roleHierarchyService.createRoleHierarchy(request2);
		roleHierarchyService.createRoleHierarchy(request3);

		// when
		List<RoleHierarchy> roleHierarchies = roleHierarchyQueryService.findRoleHierarchies();

		// then
		assertThat(roleHierarchies).hasSize(3);
	}

	@Test
	@DisplayName("권한 계층을 전체 조회할 시 데이터가 없으면 비어있는 리스트를 반환한다.")
	void findRoleHierarchiesWithEmptyList() {
		// given

		// when
		List<RoleHierarchy> roleHierarchies = roleHierarchyQueryService.findRoleHierarchies();

		// then
		assertThat(roleHierarchies).hasSize(0);
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