package com.my.authserver.domain.repository.member.auth;

import com.my.authserver.domain.entity.member.auth.MemberRoles;
import com.my.authserver.domain.entity.member.auth.embedded.MemberRolesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRolesRepository extends JpaRepository<MemberRoles, MemberRolesId> {
}
