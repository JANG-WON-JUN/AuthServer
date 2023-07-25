package com.my.authserver.domain.repository.member.impl;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.QMember;
import com.my.authserver.domain.repository.member.MemberQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.my.authserver.domain.entity.member.QMember.member;
import static com.my.authserver.utils.CommonUtils.nullSafeBuilder;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findByEmail(String email) {
        Member member = queryFactory
                .selectFrom(QMember.member)
                .where(emailEq(email))
                .fetchOne();

        return ofNullable(member).orElse(null);
    }

    @Override
    public Member findByNickname(String nickname) {
        Member member = queryFactory
                .selectFrom(QMember.member)
                .where(nicknameEq(nickname))
                .fetchOne();

        return ofNullable(member).orElse(null);
    }

    private BooleanBuilder emailEq(String email) {
        return nullSafeBuilder(() -> member.email.eq(email));
    }

    private BooleanBuilder nicknameEq(String nickname) {
        return nullSafeBuilder(() -> member.nickname.eq(nickname));
    }
}
