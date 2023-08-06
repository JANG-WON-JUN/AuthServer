package com.my.authserver.member.auth.repository;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.web.searchcondition.RoleSearchCondition;
import com.my.authserver.member.enums.RoleType;
import com.my.authserver.support.repository.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static com.my.authserver.member.enums.RoleType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class RoleRepositoryTest extends RepositoryTestSupport {

    @Test
    @DisplayName("권한 이름으로 권한 객체를 조회할 수 있다.")
    void findByRoleName() {
        // given
        RoleType roleType = ROLE_ANONYMOUS;

        Role role1 = createRole(roleType);

        roleRepository.save(role1);

        // when
        Role savedRole = roleRepository.findByRoleType(roleType).get();

        // then
        assertThat(savedRole.getId()).isNotNull();
        assertThat(savedRole.getRoleType()).isEqualByComparingTo(roleType);
    }

    @Test
    @DisplayName("권한 이름으로 권한 조회 시 권한이 존재하지 않으면 조회할 수 없다.")
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

        Role role1 = createRole(roleType1);
        Role role2 = createRole(roleType2);
        Role role3 = createRole(roleType3);

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        RoleSearchCondition condition = createRoleSearchCondition(null);

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

        Role role1 = createRole(roleType1);
        Role role2 = createRole(roleType2);
        Role role3 = createRole(roleType3);

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        RoleSearchCondition condition = createRoleSearchCondition("회원");

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
    @DisplayName("권한 목록이 조회되지 않는 페이지 번호를 가지고 권한 목록을 조회 시 조회결과는 없다.")
    void findRolesWithConditionWithInvalidPage() {
        // given
        RoleSearchCondition condition = createRoleSearchCondition(null);

        // when
        Page<Role> page = roleRepository.findRolesWithCondition(condition);

        // then
        List<Role> savedRoles = page.getContent();

        assertThat(savedRoles).isEmpty();
    }

    private Role createRole(RoleType roleType) {
        return Role.builder()
                .roleType(roleType)
                .roleDesc(roleType.getRoleDesc())
                .build();
    }

    private RoleSearchCondition createRoleSearchCondition(String keyword) {
        return RoleSearchCondition.builder()
                .page(0)
                .keyword(keyword)
                .build();
    }
}