package com.my.authserver.member.auth.repository;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.ResourceType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResourceQueryRepository {

    List<Resource> findByResourceType(ResourceType resourceType);

    Page<Resource> findResourceWithCondition(ResourceSearchCondition condition);
}
