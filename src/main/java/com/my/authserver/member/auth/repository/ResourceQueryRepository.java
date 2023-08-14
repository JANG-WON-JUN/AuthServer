package com.my.authserver.member.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

public interface ResourceQueryRepository {

	List<Resource> findByResourceType(ResourceType resourceType);

	Optional<Resource> findResource(String resourceName, HttpMethod httpMethod, ResourceType resourceType);

	Page<Resource> findResourcesWithCondition(ResourceSearchCondition condition);
}
