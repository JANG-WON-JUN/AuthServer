package com.my.authserver.member.auth.web;

import static com.my.authserver.member.enums.HttpMethod.*;
import static com.my.authserver.member.enums.ResourceType.*;
import static java.nio.charset.StandardCharsets.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.my.authserver.domain.entity.member.auth.Resource;
import com.my.authserver.member.auth.service.request.ResourceUpdateServiceRequest;
import com.my.authserver.member.auth.web.request.ResourceCreateRequest;
import com.my.authserver.member.auth.web.request.ResourceUpdateRequest;
import com.my.authserver.member.auth.web.searchcondition.ResourceSearchCondition;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import com.my.authserver.support.controller.ControllerTestSupport;

class ResourceControllerTest extends ControllerTestSupport {

	@Test
	@DisplayName("자원 생성 시 필요한 정보를 받아 자원을 생성한다.")
	void createResourceRequest() throws Exception {
		// given
		ResourceCreateRequest request = createResourceRequest("/resource", GET, URL);

		// expected
		mockMvc.perform(post("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists());
	}

	@Test
	@DisplayName("자원 생성 시 자원 설명은 필수 입력이다.")
	void createResourceWithNoResourceName() throws Exception {
		// given
		ResourceCreateRequest request = createResourceRequest("  ", GET, URL);

		// expected
		mockMvc.perform(post("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("자원 생성 시 http 메서드는 필수 입력이다.")
	void createResourceWithNoHttpMethod() throws Exception {
		// given
		ResourceCreateRequest request = createResourceRequest("/resource", null, URL);

		// expected
		mockMvc.perform(post("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("자원 생성 시 자원 타입는 필수 입력이다.")
	void createResourceWithNoResourceType() throws Exception {
		// given
		ResourceCreateRequest request = createResourceRequest("/resource", GET, null);

		// expected
		mockMvc.perform(post("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errorCode").value(BAD_REQUEST.value()));
	}

	@Test
	@DisplayName("조회 조건을 적용하여 자원 목록을 조회한다.")
	void findResourcesWithCondition() throws Exception {
		// given
		ResourceSearchCondition condition = createResourceSearchCondition(null);

		given(resourceQueryService.findResourcesWithCondition(any(ResourceSearchCondition.class)))
			.willReturn(Page.empty());

		// expected
		mockMvc.perform(get("/admin/api/resources")
				.characterEncoding(UTF_8)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(condition)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.size()").value(0));
	}

	@Test
	@DisplayName("자원의 id와 수정할 데이터를 받아 자원을 수정한다.")
	void updateResource() throws Exception {
		// given
		Resource resource = createResource("/resource", GET, URL);
		ResourceUpdateRequest updateRequest = updateResourceRequest(1L, "/resource/new", POST, URL);

		given(resourceService.updateResource(any(ResourceUpdateServiceRequest.class)))
			.willReturn(1L);

		// expected
		mockMvc.perform(patch("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("자원을 수정 시 자원 id는 필수 입력이다.")
	void updateResourceWithNoId() throws Exception {
		// given
		ResourceUpdateRequest updateRequest = updateResourceRequest(null, "/resource/new", POST, URL);

		// expected
		mockMvc.perform(patch("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("자원을 수정 시 자원 설명은 필수 입력이다.")
	void updateResourceWithNoResourceName() throws Exception {
		// given
		ResourceUpdateRequest updateRequest = updateResourceRequest(1L, "   ", POST, URL);

		// expected
		mockMvc.perform(patch("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("자원을 수정 시 http 메서드는 필수 입력이다.")
	void updateResourceWithNoHttpMethod() throws Exception {
		// given
		ResourceUpdateRequest updateRequest = updateResourceRequest(1L, "/resource/new", null, URL);

		// expected
		mockMvc.perform(patch("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("자원을 수정 시 자원 타입은 필수 입력이다.")
	void updateResourceWithNoResourceType() throws Exception {
		// given
		ResourceUpdateRequest updateRequest = updateResourceRequest(1L, "/resource/new", POST, null);

		// expected
		mockMvc.perform(patch("/admin/api/resources")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("자원의 id를 입력받아 자원을 삭제한다.")
	void deleteResource() throws Exception {
		// given
		// expected
		mockMvc.perform(delete("/admin/api/resources/1")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	private ResourceCreateRequest createResourceRequest(String resourceName, HttpMethod httpMethod,
		ResourceType resourceType) {
		ResourceCreateRequest request = new ResourceCreateRequest();

		request.setResourceName(resourceName);
		request.setHttpMethod(httpMethod);
		request.setResourceType(resourceType);

		return request;
	}

	private ResourceUpdateRequest updateResourceRequest
		(Long id, String resourceName, HttpMethod httpMethod, ResourceType resourceType) {

		ResourceUpdateRequest request = new ResourceUpdateRequest();

		request.setId(id);
		request.setResourceName(resourceName);
		request.setHttpMethod(httpMethod);
		request.setResourceType(resourceType);

		return request;
	}

	private ResourceSearchCondition createResourceSearchCondition(String keyword) {
		return ResourceSearchCondition.builder()
			.page(0)
			.keyword(keyword)
			.build();
	}

	private Page<Resource> createPage() {
		Resource resource1 = createResource("/resource1", GET, URL);
		Resource resource2 = createResource("/resource2", POST, URL);
		Resource resource3 = createResource("/resource3", DELETE, URL);

		List<Resource> resources = List.of(resource1, resource2, resource3);

		return new PageImpl<>(resources, Pageable.ofSize(10), 0);
	}

	private Resource createResource(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		return Resource.builder()
			.resourceName(resourceName)
			.httpMethod(httpMethod)
			.resourceType(resourceType)
			.build();
	}
}