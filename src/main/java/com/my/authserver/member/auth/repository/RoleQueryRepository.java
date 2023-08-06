package com.my.authserver.member.auth.repository;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.web.searchcondition.RoleSearchCondition;
import org.springframework.data.domain.Page;

public interface RoleQueryRepository {

    Page<Role> findRolesWithCondition(RoleSearchCondition condition);
}
