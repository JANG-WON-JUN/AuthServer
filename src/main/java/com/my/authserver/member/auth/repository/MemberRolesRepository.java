package com.my.authserver.member.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.MemberRoles;
import com.my.authserver.domain.entity.member.auth.embedded.MemberRolesId;

public interface MemberRolesRepository extends JpaRepository<MemberRoles, MemberRolesId> {
}
