package com.my.authserver.member.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.ResourceAlreadyExists;
import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.repository.ResourceRepository;
import com.my.authserver.member.auth.service.query.ResourceQueryService;
import com.my.authserver.member.auth.service.request.ResourceCreateServiceRequest;
import com.my.authserver.member.auth.service.request.ResourceUpdateServiceRequest;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourceService {

	private final MessageSourceUtils messageSourceUtils;
	private final ResourceQueryService resourceQueryService;
	private final ResourceRepository resourcesRepository;

	public Long createResource(ResourceCreateServiceRequest request) {
		validate(request.getResourceName(), request.getHttpMethod(), request.getResourceType());

		Resource savedResource = resourcesRepository.save(request.toEntity());

		return savedResource.getId();
	}

	public Long updateResource(ResourceUpdateServiceRequest request) {
		validate(request.getResourceName(), request.getHttpMethod(), request.getResourceType());

		Resource savedResource = resourceQueryService.findById(request.getId());

		savedResource.changeResourceName(request.getResourceName());
		savedResource.changeHttpMethod(request.getHttpMethod());
		savedResource.changeResourceType(request.getResourceType());

		return savedResource.getId();
	}

	public void deleteResource(Long id) {
		Long savedResourceId = resourceQueryService.findById(id).getId();
		resourcesRepository.deleteById(savedResourceId);
	}

	private void validate(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		Assert.hasText(resourceName, messageSourceUtils.getMessage("field.required.resourceName"));
		Assert.notNull(httpMethod, messageSourceUtils.getMessage("field.required.httpMethod"));
		Assert.notNull(resourceType, messageSourceUtils.getMessage("field.required.resourceType"));

		if (resourceQueryService.exists(resourceName, httpMethod, resourceType)) {
			throw new ResourceAlreadyExists(messageSourceUtils.getMessage("error.resourceAlreadyExist"));
		}
	}
}
