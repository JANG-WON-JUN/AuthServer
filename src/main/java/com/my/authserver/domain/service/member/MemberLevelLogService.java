package com.my.authserver.domain.service.member;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.MemberLevelLog;
import com.my.authserver.domain.repository.member.MemberLevelLogRepository;
import com.my.authserver.domain.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLevelLogService {

    private final MemberRepository memberRepository;
    private final MemberLevelLogRepository memberLevelLogRepository;

    public void createLevelLog(Member member, MemberLevelLog memberLevelLog){
        memberLevelLog.setMember(member);
        memberLevelLogRepository.save(memberLevelLog);
    }

    public List<MemberLevelLog> findByEmail(String email){
        Member saveMember = memberRepository.findByEmail(email);
        return memberLevelLogRepository.findByMember(saveMember);
    }
}
