package com.my.authserver.member.auth.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.dto.ResourceNotFound;
import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.repository.ResourceRepository;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceQueryService {

	private final MessageSourceUtils messageSourceUtils;
	private final ResourceRepository resourceRepository;

	public List<Resource> findByResourceType(ResourceType resourceType) {
		return resourceRepository.findByResourceType(resourceType);
	}

	public Page<Resource> findResourcesWithCondition(ResourceSearchCondition condition) {
		return resourceRepository.findResourcesWithCondition(condition);
	}

	public Resource findById(Long id) {
		return getResourceFrom(resourceRepository.findById(id));
	}

	public Resource findResource(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		return getResourceFrom(resourceRepository.findResource(resourceName, httpMethod, resourceType));
	}

	public boolean exists(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		Optional<Resource> optional = resourceRepository.findResource(resourceName, httpMethod, resourceType);
		return optional.isPresent();
	}

	private Resource getResourceFrom(Optional<Resource> resourceOptional) {
		return resourceOptional.orElseThrow(()
			-> new ResourceNotFound(messageSourceUtils.getMessage("error.noResource")));
	}

}
