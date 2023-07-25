package com.my.authserver.domain.service.member.auth;

import com.my.authserver.domain.entity.member.auth.Resources;
import com.my.authserver.domain.enums.ResourceType;
import com.my.authserver.domain.repository.member.auth.ResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;

    public void createResources(Resources resources){
        resourcesRepository.save(resources);
    }

    public List<Resources> findByResourceType(ResourceType resourceType){
        return resourcesRepository.findByResourceType(resourceType);
    }
}
