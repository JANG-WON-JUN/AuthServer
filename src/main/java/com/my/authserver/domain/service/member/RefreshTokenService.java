package com.my.authserver.domain.service.member;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.RefreshToken;
import com.my.authserver.domain.repository.member.MemberRepository;
import com.my.authserver.domain.repository.member.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    // MemberService로 변경하면 순환참조 오류 발생하므로 MemberRepository로 선언
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(RefreshToken refreshToken, String email){
        Member savedMember = memberRepository.findByEmail(email);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        savedMember.changeRefreshToken(savedRefreshToken);
    }
}
