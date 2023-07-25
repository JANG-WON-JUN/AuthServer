package com.my.authserver.domain.repository.member;


import com.my.authserver.domain.entity.member.Member;

public interface MemberQueryRepository {

    Member findByEmail(String email);

    Member findByNickname(String nickname);
}
