package com.my.authserver.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.RefreshToken;
import com.my.authserver.member.repository.MemberRepository;
import com.my.authserver.member.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

	// MemberService로 변경하면 순환참조 오류 발생하므로 MemberRepository로 선언
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public void createRefreshToken(RefreshToken refreshToken, String email) {
		Member savedMember = memberRepository.findByEmail(email);
		RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

		savedMember.changeRefreshToken(savedRefreshToken);
	}
}
