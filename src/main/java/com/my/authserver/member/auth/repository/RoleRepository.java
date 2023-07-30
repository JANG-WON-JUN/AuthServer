package com.my.authserver.member.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.enums.Roles;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleName(Roles role);
}
