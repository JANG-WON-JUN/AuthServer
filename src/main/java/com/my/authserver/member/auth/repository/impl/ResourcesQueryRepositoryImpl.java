package com.my.authserver.member.auth.repository.impl;

import static com.my.authserver.common.utils.CommonUtils.*;
import static com.my.authserver.domain.entity.member.auth.QResources.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.my.authserver.domain.entity.member.auth.Resources;
import com.my.authserver.member.auth.repository.ResourcesQueryRepository;
import com.my.authserver.member.enums.ResourceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResourcesQueryRepositoryImpl implements ResourcesQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Resources> findByResourceType(ResourceType resourceType) {
		return queryFactory
			.selectFrom(resources)
			.where(resourceTypeEq(resourceType))
			.orderBy(resources.orderNum.asc())
			.fetch();
	}

	private BooleanBuilder resourceTypeEq(ResourceType resourceType) {
		return nullSafeBuilder(() -> resources.resourceType.eq(resourceType));
	}

}
