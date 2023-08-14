package com.my.authserver.member.auth.repository;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.domain.Page;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.support.repository.RepositoryTestSupport;

class ResourceRepositoryTest extends RepositoryTestSupport {

	@ParameterizedTest
	@EnumSource(ResourceType.class)
	@DisplayName("자원 타입으로 자원 리스트를 조회할 수 있다.")
	void findByResourceType(ResourceType resourceType) {
		// given
		Resource resource = createResource("", GET, resourceType);

		resourcesRepository.save(resource);

		// when
		List<Resource> resources = resourcesRepository.findByResourceType(resourceType);

		// then
		assertThat(resources).hasSize(1)
			.extracting("httpMethod", "resourceType")
			.containsExactlyInAnyOrder(
				tuple(GET, resourceType)
			);
	}

	@ParameterizedTest
	@EnumSource(ResourceType.class)
	@DisplayName("입력한 자원 타입으로 등록된 자원이 없으면 조회결과는 없다.")
	void findByResourceTypeWithNoResourceType(ResourceType resourceType) {
		// given

		// when
		List<Resource> resources = resourcesRepository.findByResourceType(resourceType);

		// then
		assertThat(resources).isEmpty();
	}

	@Test
	@DisplayName("조회 조건을 적용하여 자원 목록을 조회한다.")
	void findResourceWithCondition() {
		// given
		Resource resource1 = createResource("/admin/resources", GET, URL);
		Resource resource2 = createResource("/admin/resources", POST, URL);

		resourcesRepository.saveAll(List.of(resource1, resource2));

		ResourceSearchCondition condition = createResourceSearchCondition(0, "admin", GET, URL);

		// when
		Page<Resource> page = resourcesRepository.findResourcesWithCondition(condition);

		// then
		List<Resource> resources = page.getContent();

		assertThat(resources).hasSize(1)
			.extracting("httpMethod", "resourceType")
			.containsExactlyInAnyOrder(
				tuple(GET, URL)
			);
	}

	@Test
	@DisplayName("조회 조건을 만족하는 자원이 없으면 조회결과는 없다.")
	void findResourceWithInValidCondition() {
		// given
		Resource resource1 = createResource("/admin/resources", GET, URL);
		Resource resource2 = createResource("/admin/resources", POST, URL);

		resourcesRepository.saveAll(List.of(resource1, resource2));

		ResourceSearchCondition condition = createResourceSearchCondition(0, "member", null, null);

		// when
		Page<Resource> page = resourcesRepository.findResourcesWithCondition(condition);

		// then
		List<Resource> resources = page.getContent();

		assertThat(resources).isEmpty();
	}

	@Test
	@DisplayName("자원 목록이 조회되지 않는 페이지 번호를 가지고 자원 목록 조회 시 첫 페이지가 조회된다.")
	void findResourceWithInValidPage() {
		// given
		Resource resource1 = createResource("/admin/resources", GET, URL);
		Resource resource2 = createResource("/admin/resources", POST, URL);

		resourcesRepository.saveAll(List.of(resource1, resource2));

		ResourceSearchCondition condition = createResourceSearchCondition(0, "admin", GET, URL);

		// when
		Page<Resource> page = resourcesRepository.findResourcesWithCondition(condition);

		// then
		List<Resource> resources = page.getContent();

		assertThat(resources).hasSize(1)
			.extracting("resourceName", "httpMethod", "resourceType")
			.containsExactlyInAnyOrder(
				tuple("/admin/resources", GET, URL)
			);
	}

	@Test
	@DisplayName("자원 설명, 자원타입, http 메소드로 자원 1개를 조회한다.")
	void findResource() {
		// given
		String resourceName = "/admin/resources";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		Resource resource = createResource(resourceName, httpMethod, resourceType);

		resourcesRepository.save(resource);

		// when
		Optional<Resource> optional = resourcesRepository.findResource(resourceName, httpMethod, resourceType);

		// then
		Resource savedResource = optional.get();
		assertThat(savedResource.getId()).isNotNull();
		assertThat(savedResource.getHttpMethod()).isEqualTo(httpMethod);
		assertThat(savedResource.getResourceType()).isEqualTo(resourceType);
	}

	@Test
	@DisplayName("조회 조건을 만족하는 자원이 없으면 조회결과는 없다.")
	void findResourceWithNoResult() {
		// given
		String resourceName = "/admin/resources";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		Resource resource = createResource(resourceName, httpMethod, resourceType);

		resourcesRepository.save(resource);

		// when
		Optional<Resource> optional = resourcesRepository.findResource("존재하지 않는 조회조건", httpMethod, resourceType);

		// then
		assertThat(optional.isEmpty()).isTrue();
	}

	private Resource createResource(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		return Resource.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}

	private ResourceSearchCondition createResourceSearchCondition
		(int page, String keyword, HttpMethod httpMethod, ResourceType resourceType) {
		return ResourceSearchCondition.builder()
			.page(page)
			.keyword(keyword)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}
}