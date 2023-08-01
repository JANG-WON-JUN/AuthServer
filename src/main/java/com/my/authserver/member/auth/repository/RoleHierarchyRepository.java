package com.my.authserver.member.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.authserver.domain.entity.member.auth.RoleHierarchy;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
}
