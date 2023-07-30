package com.my.authserver.member.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.my.authserver.domain.entity.member.auth.Resources;
import com.my.authserver.member.auth.repository.ResourcesRepository;
import com.my.authserver.member.enums.ResourceType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourcesService {

	private final ResourcesRepository resourcesRepository;

	public void createResources(Resources resources) {
		resourcesRepository.save(resources);
	}

	public List<Resources> findByResourceType(ResourceType resourceType) {
		return resourcesRepository.findByResourceType(resourceType);
	}
}
