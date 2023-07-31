package com.my.authserver.member.auth.repository;

import org.springframework.data.domain.Page;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.web.request.RoleSearchCondition;

public interface RoleQueryRepository {

	Page<Role> findRolesWithCondition(RoleSearchCondition condition);
}
