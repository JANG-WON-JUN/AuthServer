package com.my.authserver.domain.repository.member.auth;


import com.my.authserver.domain.entity.member.auth.Resources;
import com.my.authserver.domain.enums.ResourceType;

import java.util.List;

public interface ResourcesQueryRepository {

    List<Resources> findByResourceType(ResourceType resourceType);
}
