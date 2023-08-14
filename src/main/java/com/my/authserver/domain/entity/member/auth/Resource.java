package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	resourceName : 자원의 이름
		1) ResourceType이 url인 경우 -> resourceName은 /admin/** 같은 url 형태로 저장
		2) ResourceType이 method인 경우 -> resourceName은 자바의 method명을 저장
	HttpMethod : http 메서드
		1) http method가 null 인 경우 모든 http method에 대한 자원이다.
 */
@Entity
@Table(name = "RESOURCES_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String resourceName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10)")
	private HttpMethod httpMethod;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10)")
	private ResourceType resourceType;

	@Builder
	private Resource(String resourceName, HttpMethod httpMethod, ResourceType resourceType) {
		this.resourceName = resourceName;
		this.httpMethod = httpMethod;
		this.resourceType = resourceType;
	}

	public void changeResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void changeHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void changeResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
}
