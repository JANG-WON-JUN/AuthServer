package com.my.authserver.member.repository;

import com.my.authserver.domain.entity.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
}
