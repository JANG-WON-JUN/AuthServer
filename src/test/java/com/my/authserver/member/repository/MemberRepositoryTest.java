package com.my.authserver.member.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyDataJpaTest;
import com.my.authserver.domain.entity.member.Member;

@MyDataJpaTest
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("이메일로 회원을 조회할 수 있다.")
	void findByEmail() {
		// given
		String email = "admin@gmail.com";
		Member member = createMember(email, "");

		memberRepository.save(member);

		// when
		Member savedMember = memberRepository.findByEmail(email);

		// then
		assertThat(savedMember.getId()).isNotNull();
		assertThat(savedMember.getEmail()).isEqualTo(email);
	}

	@Test
	@DisplayName("이메일로 회원을 조회 시 회원이 존재하지 않으면 조회할 수 없다.")
	void findByEmailWithNoMember() {
		// given
		String email = "admin@gmail.com";

		// when
		Member savedMember = memberRepository.findByEmail(email);

		// then
		assertThat(savedMember).isNull();
	}

	@Test
	@DisplayName("닉네임으로 회원을 조회 시 회원이 존재하면 화원 정보를 조회한다.")
	void findByNickname() {
		// given
		String nickname = "닉네임";
		Member member = createMember("admin@gamil.com", nickname);

		memberRepository.save(member);

		// when
		Member savedMember = memberRepository.findByNickname(nickname);

		// then
		assertThat(savedMember.getId()).isNotNull();
		assertThat(savedMember.getNickname()).isEqualTo(nickname);
	}

	@Test
	@DisplayName("닉네임으로 회원을 조회 시 회원이 존재하지 않으면 null을 반환한다.")
	void findByNicknameWithNoMember() {
		// given
		String nickname = "닉네임";

		// when
		Member savedMember = memberRepository.findByNickname(nickname);

		// then
		assertThat(savedMember).isNull();
	}

	private Member createMember(String email, String nickname) {
		return Member.builder()
			.email(email)
			.nickname(nickname)
			.build();
	}
}