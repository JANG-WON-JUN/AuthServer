package com.my.authserver.member.auth.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.RoleType;

public interface RoleResourceQueryRepository {

	List<RoleResource> findByRoleType(RoleType roleType);

	Page<RoleResource> findRoleResourcesWithCondition(RoleResourceSearchCondition condition);

}
