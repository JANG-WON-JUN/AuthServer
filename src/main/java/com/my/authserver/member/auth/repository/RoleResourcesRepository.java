package com.my.authserver.member.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.RoleResources;
import com.my.authserver.domain.entity.member.auth.embedded.RoleResourcesId;
import com.my.authserver.member.enums.Roles;

public interface RoleResourcesRepository extends JpaRepository<RoleResources, RoleResourcesId> {

	// QueryDsl에서 EmbeddedId를 지원하지 않으므로 Query Methods로 처리
	List<RoleResources> findByIdRoleRoleName(Roles role);

}
