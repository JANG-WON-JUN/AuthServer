package com.my.authserver.member.auth.repository.impl;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.repository.ResourceQueryRepository;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.my.authserver.common.utils.CommonUtils.nullSafeBuilder;
import static com.my.authserver.domain.entity.member.auth.QResource.resource;

@Repository
@RequiredArgsConstructor
public class ResourceQueryRepositoryImpl implements ResourceQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Resource> findByResourceType(ResourceType resourceType) {
        return queryFactory
                .selectFrom(resource)
                .where(resourceTypeEq(resourceType))
                .fetch();
    }

    @Override
    public Page<Resource> findResourceWithCondition(ResourceSearchCondition condition) {
        List<Resource> resources = queryFactory
                .selectFrom(resource)
                .where(
                        resourceTypeEq(condition.getResourceType()),
                        httpMethodEq(condition.getHttpMethod()),
                        resourceNameContains(condition.getKeyword())
                )
                .offset(condition.getPageable().getOffset())
                .limit(condition.getPageable().getPageSize())
                .orderBy(resource.id.desc())
                .fetch();

        long totals = queryFactory
                .selectFrom(resource)
                .where(
                        resourceTypeEq(condition.getResourceType()),
                        httpMethodEq(condition.getHttpMethod()),
                        resourceNameContains(condition.getKeyword())
                )
                .offset(condition.getPageable().getOffset())
                .limit(condition.getPageable().getPageSize())
                .fetch()
                .size();

        return new PageImpl<>(resources, condition.getPageable(), totals);
    }

    private BooleanBuilder resourceTypeEq(ResourceType resourceType) {
        return nullSafeBuilder(() -> resource.resourceType.eq(resourceType));
    }

    private BooleanBuilder httpMethodEq(HttpMethod httpMethod) {
        return nullSafeBuilder(() -> resource.httpMethod.eq(httpMethod));
    }

    private BooleanBuilder resourceNameContains(String keyword) {
        return nullSafeBuilder(() -> resource.resourceName.containsIgnoreCase(keyword));
    }

}
