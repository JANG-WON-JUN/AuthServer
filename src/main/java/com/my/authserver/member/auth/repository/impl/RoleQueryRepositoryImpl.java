package com.my.authserver.member.auth.repository.impl;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleQueryRepository;
import com.my.authserver.member.auth.web.searchcondition.RoleSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.my.authserver.common.utils.CommonUtils.nullSafeBuilder;
import static com.my.authserver.domain.entity.member.auth.QRole.role;

@Repository
@RequiredArgsConstructor
public class RoleQueryRepositoryImpl implements RoleQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Role> findRolesWithCondition(RoleSearchCondition condition) {
        List<Role> roles = queryFactory
                .selectFrom(role)
                .where(
                        roleDescLike(condition.getKeyword())
                )
                .offset(condition.getPageable().getOffset())
                .limit(condition.getPageable().getPageSize())
                .orderBy(role.id.desc())
                .fetch();

        long totals = queryFactory
                .selectFrom(role)
                .where(
                        roleDescLike(condition.getKeyword())
                )
                .offset(condition.getPageable().getOffset())
                .limit(condition.getPageable().getPageSize())
                .fetch()
                .size();

        return new PageImpl<>(roles, condition.getPageable(), totals);
    }

    private BooleanBuilder roleDescLike(String keyword) {
        return nullSafeBuilder(() -> role.roleDesc.contains(keyword));
    }
}
