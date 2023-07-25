package com.my.authserver.domain.repository.member;


import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.MemberLevelLog;

import java.util.List;

public interface MemberLevelLogQueryRepository {

    List<MemberLevelLog> findByMember(Member member);
}
