package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleAlreadyExists;
import com.my.authserver.common.web.exception.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.auth.service.request.RoleCreateServiceRequest;
import com.my.authserver.member.auth.service.request.RoleUpdateServiceRequest;
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
		RoleCreateServiceRequest request = createRequest(roleType);

		// when
		roleService.createRole(request);

		// then
		Role savedRole = roleQueryService.findByRoleType(roleType);

		assertThat(savedRole.getId()).isNotNull();
		assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
	}

	@Test
	@DisplayName("이미 존재하는 권한은 생성할 수 없다.")
	void createRoleWithExistsRole() {
		// given
		RoleType roleType = RoleType.ROLE_ANONYMOUS;
		RoleCreateServiceRequest request = createRequest(roleType);

		roleService.createRole(request);

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(RoleAlreadyExists.class)
			.hasMessage(messageSourceUtils.getMessage("error.roleAlreadyExist"));
	}

	@Test
	@DisplayName("권한 생성 시 권한 타입은 필수 입력이다.")
	void createRoleWithNoRoleType() {
		// given
		RoleCreateServiceRequest request = createRequest(null, "권한 설명");

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.roleType"));
	}

	@Test
	@DisplayName("권한 생성 시 권한 설명은 필수 입력이다.")
	void createRoleWithNoRoleDesc() {
		// given
		RoleCreateServiceRequest request = createRequest(ROLE_ADMIN, null);

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.roleDesc"));
	}

	@Test
	@DisplayName("권한 생성 시 권한 설명에 공백이나 빈 문자열을 입력할 수 없다.")
	void createRoleWithWhiteSpaceRoleDesc() {
		// given
		RoleCreateServiceRequest request = createRequest(ROLE_ADMIN, " ");

		// expected
		assertThatThrownBy(() -> roleService.createRole(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.roleDesc"));
	}

	@Test
	@DisplayName("권한의 id로 권한을 삭제한다.")
	void deleteRole() {
		// given
		RoleType roleType = ROLE_ADMIN;
		RoleCreateServiceRequest request = createRequest(roleType);

		Long savedRoleId = roleService.createRole(request);

		// when
		roleService.deleteRole(savedRoleId);

		// then
		List<Role> roles = roleRepository.findAll();

		assertThat(roles).isEmpty();
	}

	@Test
	@DisplayName("권한의 id와 새로운 권한 설명을 받아 권한 설명을 수정한다.")
	void updateRole() {
		// given
		RoleType roleType = RoleType.ROLE_ANONYMOUS;
		String newRoleDesc = "새로운 비회원 설명";
		RoleCreateServiceRequest request = createRequest(roleType);
		RoleUpdateServiceRequest updateRequest = updateRequest(newRoleDesc);

		roleService.createRole(request);

		// when
		Long savedRoleId = roleService.updateRole(updateRequest);

		// then
		Role savedRole = roleQueryService.findById(savedRoleId);
		assertThat(savedRole.getRoleDesc()).isEqualTo(newRoleDesc);
	}

	@Test
	@DisplayName("권한 설명을 수정 시 권한 설명은 필수입력이다.")
	void updateRoleWithNoRoleDesc() {
		// given
		createRequest(ROLE_ADMIN);
		RoleUpdateServiceRequest updateRequest = updateRequest(" ");

		// expected
		assertThatThrownBy(() -> roleService.updateRole(updateRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.roleDesc"));
	}

	@Test
	@DisplayName("존재하지 않는 권한의 id로 권한을 삭제할 수 없다.")
	void deleteRoleWithNoRoleId() {
		// given
		Long notExistsRoleId = 1L;

		// expected
		assertThatThrownBy(() -> roleService.deleteRole(notExistsRoleId))
			.isInstanceOf(RoleNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noRole"));
	}

	private RoleCreateServiceRequest createRequest(RoleType roleType) {
		return createRequest(roleType, roleType.getRoleDesc());
	}

	private RoleCreateServiceRequest createRequest(RoleType roleType, String roleDesc) {
		return RoleCreateServiceRequest.builder()
			.roleType(roleType)
			.roleDesc(roleDesc)
			.build();
	}

	private RoleUpdateServiceRequest updateRequest(String roleDesc) {
		return RoleUpdateServiceRequest.builder()
			.id(1L)
			.roleDesc(roleDesc)
			.build();
	}
}