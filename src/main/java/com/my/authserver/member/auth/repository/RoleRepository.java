package com.my.authserver.member.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleQueryRepository {

	Optional<Role> findByRoleType(RoleType roleType);
}
