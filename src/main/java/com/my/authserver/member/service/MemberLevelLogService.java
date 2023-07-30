package com.my.authserver.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.MemberLevelLog;
import com.my.authserver.member.repository.MemberLevelLogRepository;
import com.my.authserver.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLevelLogService {

	private final MemberRepository memberRepository;
	private final MemberLevelLogRepository memberLevelLogRepository;

	public void createLevelLog(Member member, MemberLevelLog memberLevelLog) {
		memberLevelLog.setMember(member);
		memberLevelLogRepository.save(memberLevelLog);
	}

	public List<MemberLevelLog> findByEmail(String email) {
		Member saveMember = memberRepository.findByEmail(email);
		return memberLevelLogRepository.findByMember(saveMember);
	}
}
