package com.my.authserver.member.auth.repository;

import com.my.authserver.domain.entity.member.auth.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long>, ResourceQueryRepository {
}
