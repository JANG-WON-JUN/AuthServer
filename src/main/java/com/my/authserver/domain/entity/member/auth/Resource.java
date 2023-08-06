package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	resourceName : 자원의 이름
		1) ResourceType이 url인 경우 -> resourceName은 /admin/** 같은 url 형태로 저장
		2) ResourceType이 method인 경우 -> resourceName은 자바의 method명을 저장
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
}
