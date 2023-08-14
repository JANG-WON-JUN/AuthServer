package com.my.authserver.member.auth.service;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.domain.Page;

import com.my.authserver.common.web.exception.ResourceAlreadyExists;
import com.my.authserver.common.web.exception.dto.ResourceNotFound;
import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.service.request.ResourceCreateServiceRequest;
import com.my.authserver.member.auth.service.request.ResourceUpdateServiceRequest;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.support.service.ServiceTestSupport;

class ResourceServiceTest extends ServiceTestSupport {

	@Test
	@DisplayName("자원 설명, http 메소드, 자원 타입을 입력받아 자원을 생성한다.")
	void createResource() {
		// given
		String resourceName = "/resource";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		ResourceCreateServiceRequest request = createResource(resourceName, httpMethod, resourceType);

		// when
		Long savedResourceId = resourceService.createResource(request);

		// then
		Resource savedResource = resourceQueryService.findById(savedResourceId);
		assertThat(savedResource.getId()).isNotNull();
		assertThat(savedResource.getResourceName()).isEqualTo(resourceName);
		assertThat(savedResource.getHttpMethod()).isEqualTo(httpMethod);
		assertThat(savedResource.getResourceType()).isEqualTo(resourceType);
	}

	@Test
	@DisplayName("이미 존재하는 자원은 생성할 수 없다.")
	void createResourceWithAlreadyExistResource() {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", GET, URL);

		resourceService.createResource(request);

		// expected
		assertThatThrownBy(() -> resourceService.createResource(request))
			.isInstanceOf(ResourceAlreadyExists.class)
			.hasMessage(messageSourceUtils.getMessage("error.resourceAlreadyExist"));
	}

	@Test
	@DisplayName("자원 생성 시 자원 설명은 필수 입력이다.")
	void createResourceWithNoResourceName() {
		// given
		ResourceCreateServiceRequest request = createResource("  ", GET, URL);

		// expected
		assertThatThrownBy(() -> resourceService.createResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.resourceName"));
	}

	@Test
	@DisplayName("자원 생성 시 http 메서드는 필수 입력이다.")
	void createResourceWithNoHttpMethod() {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", null, URL);

		// expected
		assertThatThrownBy(() -> resourceService.createResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.httpMethod"));
	}

	@Test
	@DisplayName("자원 생성 시 자원타입은 필수 입력이다.")
	void createResourceWithNoResourceType() {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", GET, null);

		// expected
		assertThatThrownBy(() -> resourceService.createResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.resourceType"));
	}

	@Test
	@DisplayName("자원 설명, http 메소드, 자원 타입을 입력받아 자원을 생성한다.")
	void updateResource() {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", GET, URL);

		Long savedResourceId = resourceService.createResource(request);

		String newResourceName = "/resource/new";
		HttpMethod newHttpMethod = ALL;
		ResourceType newResourceType = URL;

		ResourceUpdateServiceRequest updateRequest =
			updateResource(savedResourceId, newResourceName, newHttpMethod, newResourceType);

		// when
		Long updatedResourceId = resourceService.updateResource(updateRequest);

		// then
		Resource updatedResource = resourceQueryService.findById(updatedResourceId);
		assertThat(updatedResource.getId()).isNotNull();
		assertThat(updatedResource.getResourceName()).isEqualTo(newResourceName);
		assertThat(updatedResource.getHttpMethod()).isEqualTo(newHttpMethod);
		assertThat(updatedResource.getResourceType()).isEqualTo(newResourceType);
	}

	@Test
	@DisplayName("이미 존재하는 자원으로 수정 할 수 없다.")
	void updateResourceWithAlreadyExistResource() {
		// given
		String resourceName = "/resource";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		ResourceCreateServiceRequest request = createResource(resourceName, httpMethod, resourceType);

		Long savedResourceId = resourceService.createResource(request);

		ResourceUpdateServiceRequest updateRequest =
			updateResource(savedResourceId, resourceName, httpMethod, resourceType);

		// expected
		assertThatThrownBy(() -> resourceService.updateResource(updateRequest))
			.isInstanceOf(ResourceAlreadyExists.class)
			.hasMessage(messageSourceUtils.getMessage("error.resourceAlreadyExist"));
	}

	@Test
	@DisplayName("자원 수정 시 자원 설명은 필수 입력이다.")
	void updateResourceWithNoResourceName() {
		// given
		ResourceUpdateServiceRequest request = updateResource(1L, "  ", GET, URL);

		// expected
		assertThatThrownBy(() -> resourceService.updateResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.resourceName"));
	}

	@Test
	@DisplayName("자원 수정 시 http 메서드는 필수 입력이다.")
	void updateResourceWithNoHttpMethod() {
		// given
		ResourceUpdateServiceRequest request = updateResource(1L, "/resource", null, URL);

		// expected
		assertThatThrownBy(() -> resourceService.updateResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.httpMethod"));
	}

	@Test
	@DisplayName("자원 수정 시 자원타입은 필수 입력이다.")
	void updateResourceWithNoResourceType() {
		// given
		ResourceUpdateServiceRequest request = updateResource(1L, "/resource", GET, null);

		// expected
		assertThatThrownBy(() -> resourceService.updateResource(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(messageSourceUtils.getMessage("field.required.resourceType"));
	}

	@Test
	@DisplayName("자원을 id로 삭제할 수 있다.")
	void deleteResource() {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", GET, URL);

		Long savedResourceId = resourceService.createResource(request);

		// when
		resourceService.deleteResource(savedResourceId);

		// then
		assertThat(resourcesRepository.count()).isZero();
	}

	@Test
	@DisplayName("존재하지 않는 자원을 삭제할 수 없다.")
	void deleteResourceWithNoResource() {
		// expected
		assertThatThrownBy(() -> resourceService.deleteResource(1L))
			.isInstanceOf(ResourceNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noResource"));
	}

	@ParameterizedTest
	@EnumSource(ResourceType.class)
	@DisplayName("자원 타입으로 자원 리스트를 조회할 수 있다.")
	void findByResourceType(ResourceType resourceType) {
		// given
		ResourceCreateServiceRequest request = createResource("/resource", GET, resourceType);

		resourceService.createResource(request);

		// when
		List<Resource> resources = resourceQueryService.findByResourceType(resourceType);

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
		List<Resource> resources = resourceQueryService.findByResourceType(resourceType);

		assertThat(resources).isEmpty();
	}

	@Test
	@DisplayName("조회 조건을 적용하여 자원 목록을 조회한다.")
	void findResourceWithCondition() {
		// given
		ResourceCreateServiceRequest request1 = createResource("/admin/resources", GET, URL);
		ResourceCreateServiceRequest request2 = createResource("/admin/resources", POST, URL);

		resourceService.createResource(request1);
		resourceService.createResource(request2);

		ResourceSearchCondition condition = createResourceSearchCondition(0, "admin", GET, URL);

		// when
		Page<Resource> page = resourceQueryService.findResourcesWithCondition(condition);

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
		ResourceCreateServiceRequest request1 = createResource("/admin/resources", GET, URL);
		ResourceCreateServiceRequest request2 = createResource("/admin/resources", POST, URL);

		resourceService.createResource(request1);
		resourceService.createResource(request2);

		ResourceSearchCondition condition = createResourceSearchCondition(0, "member", null, null);

		// when
		Page<Resource> page = resourceQueryService.findResourcesWithCondition(condition);

		// then
		List<Resource> resources = page.getContent();

		assertThat(resources).isEmpty();
	}

	@Test
	@DisplayName("자원 목록이 조회되지 않는 페이지 번호를 가지고 자원 목록 조회 시 첫 페이지가 조회된다.")
	void findResourceWithInValidPage() {
		// given
		ResourceCreateServiceRequest request1 = createResource("/admin/resources", GET, URL);
		ResourceCreateServiceRequest request2 = createResource("/admin/resources", POST, URL);

		ResourceSearchCondition condition = createResourceSearchCondition(0, "admin", GET, URL);

		resourceService.createResource(request1);
		resourceService.createResource(request2);

		// when
		Page<Resource> page = resourceQueryService.findResourcesWithCondition(condition);

		// then
		List<Resource> resources = page.getContent();

		assertThat(resources).hasSize(1)
			.extracting("resourceName", "httpMethod", "resourceType")
			.containsExactlyInAnyOrder(
				tuple("/admin/resources", GET, URL)
			);
	}

	@Test
	@DisplayName("자원을 id로 조회할 수 있다.")
	void findById() {
		// given
		String resourceName = "/admin/resources";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		ResourceCreateServiceRequest request = createResource(resourceName, httpMethod, resourceType);

		Long savedResourceId = resourceService.createResource(request);

		// when
		Resource savedResource = resourceQueryService.findById(savedResourceId);

		// then
		assertThat(savedResource.getId()).isNotNull();
		assertThat(savedResource.getResourceName()).isEqualTo(resourceName);
		assertThat(savedResource.getHttpMethod()).isEqualTo(httpMethod);
		assertThat(savedResource.getResourceType()).isEqualTo(resourceType);
	}

	@Test
	@DisplayName("존재하지 않는 자원 id로 조회할 수 없다.")
	void findByIdWithNotValidId() {
		// given

		// when
		assertThatThrownBy(() -> resourceQueryService.findById(1L))
			.isInstanceOf(ResourceNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noResource"));
	}

	@Test
	@DisplayName("자원 설명, 자원타입, http 메소드로 자원 1개를 조회한다.")
	void findResource() {
		// given
		String resourceName = "/admin/resources";
		HttpMethod httpMethod = GET;
		ResourceType resourceType = URL;

		ResourceCreateServiceRequest request = createResource(resourceName, httpMethod, resourceType);

		resourceService.createResource(request);

		// when
		Resource savedResource = resourceQueryService.findResource(resourceName, httpMethod, resourceType);

		// then
		assertThat(savedResource.getId()).isNotNull();
		assertThat(savedResource.getHttpMethod()).isEqualTo(httpMethod);
		assertThat(savedResource.getResourceType()).isEqualTo(resourceType);
	}

	@Test
	@DisplayName("조회 조건을 만족하는 자원이 없으면 조회결과는 없다.")
	void findResourceWithNoResult() {
		// given

		// expected
		assertThatThrownBy(() -> resourceQueryService.findResource("", null, null))
			.isInstanceOf(ResourceNotFound.class)
			.hasMessage(messageSourceUtils.getMessage("error.noResource"));
	}

	@ParameterizedTest
	@CsvSource({
		"/resources,GET,URL,true",
		"/없는 자원,GET,URL,false"
	})
	@DisplayName("자원 설명, 자원타입, http 메소드로 자원 1개의 존재여부를 확인할 수 있다.")
	void exists(String resourceName, HttpMethod httpMethod, ResourceType resourceType, boolean result) {
		// given
		ResourceCreateServiceRequest request = createResource("/resources", GET, URL);

		resourceService.createResource(request);

		// when
		boolean exists = resourceQueryService.exists(resourceName, httpMethod, resourceType);

		// then
		assertThat(exists).isEqualTo(result);
	}

	private ResourceCreateServiceRequest createResource
		(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {

		return ResourceCreateServiceRequest.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}

	private ResourceUpdateServiceRequest updateResource
		(Long id, String resourceName, HttpMethod httpMethod, ResourceType resourceType) {

		return ResourceUpdateServiceRequest.builder()
			.id(id)
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