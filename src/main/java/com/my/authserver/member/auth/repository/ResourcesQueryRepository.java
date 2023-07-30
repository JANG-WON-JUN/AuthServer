package com.my.authserver.member.auth.repository;

import java.util.List;

import com.my.authserver.domain.entity.member.auth.Resources;
import com.my.authserver.member.enums.ResourceType;

public interface ResourcesQueryRepository {

	List<Resources> findByResourceType(ResourceType resourceType);
}
