package com.my.authserver.member.repository;

import com.my.authserver.domain.entity.member.Member;

public interface MemberQueryRepository {

	Member findByEmail(String email);

	Member findByNickname(String nickname);
}
