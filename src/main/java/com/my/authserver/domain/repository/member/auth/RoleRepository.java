package com.my.authserver.domain.repository.member.auth;


import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(Roles role);
}
