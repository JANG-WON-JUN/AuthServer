package com.my.authserver.member.repository;

import com.my.authserver.domain.entity.member.MemberLevelLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLevelLogRepository extends JpaRepository<MemberLevelLog, Long>, MemberLevelLogQueryRepository {
}
