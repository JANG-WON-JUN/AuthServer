package com.my.authserver.member.auth.repository.impl;

import static com.my.authserver.common.utils.CommonUtils.*;
import static com.my.authserver.domain.entity.member.auth.QRole.*;
import static com.my.authserver.domain.entity.member.auth.QRoleResource.*;
import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import com.my.authserver.domain.entity.member.auth.RoleResource;
import com.my.authserver.member.auth.repository.RoleResourceQueryRepository;
import com.my.authserver.member.auth.web.searchcondition.RoleResourceSearchCondition;
import com.my.authserver.member.enums.RoleType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoleResourceQueryRepositoryImpl implements RoleResourceQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RoleResource> findByRoleType(RoleType roleType) {
		return queryFactory
			.selectFrom(roleResource)
			.where(
				roleTypeIn(roleType)
			)
			.fetch();
	}

	@Override
	public Page<RoleResource> findRoleResourcesWithCondition(RoleResourceSearchCondition condition) {
		List<RoleResource> roleResources = queryFactory
			.selectFrom(roleResource)
			.where(
				roleTypeIn(condition.getRoleTypes())
			)
			.offset(condition.getPageable().getOffset())
			.limit(condition.getPageable().getPageSize())
			.orderBy(roleResource.id.desc())
			.fetch();

		long totals = queryFactory
			.selectFrom(roleResource)
			.where(
				roleTypeIn(condition.getRoleTypes())
			)
			.offset(condition.getPageable().getOffset())
			.limit(condition.getPageable().getPageSize())
			.fetch()
			.size();

		return new PageImpl<>(roleResources, condition.getPageable(), totals);
	}

	private BooleanBuilder roleTypeIn(RoleType roleType) {
		return nullSafeBuilder(() ->
			roleResource.role.id.in(
				select(role.id)
					.from(role)
					.where(role.roleType.eq(roleType))));
	}

	private BooleanBuilder roleTypeIn(List<RoleType> roleTypes) {
		return nullSafeBuilder(() ->
			roleResource.role.id.in(
				select(role.id)
					.from(role)
					.where(role.roleType.in(roleTypes))));
	}
}
