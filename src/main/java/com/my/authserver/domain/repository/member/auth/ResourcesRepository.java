package com.my.authserver.domain.repository.member.auth;


import com.my.authserver.domain.entity.member.auth.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long>, ResourcesQueryRepository {
}
