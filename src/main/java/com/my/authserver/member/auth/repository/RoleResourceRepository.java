package com.my.authserver.member.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.RoleResource;

public interface RoleResourceRepository extends JpaRepository<RoleResource, Long>, RoleResourceQueryRepository {

}
