package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.domain.enums.HttpMethod;
import com.my.authserver.domain.enums.ResourceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESOURCES_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resources extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    private HttpMethod httpMethod;

    private Integer orderNum; // 자원의 우선순위

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    private ResourceType resourceType;

    @Builder
    public Resources(String resourceName, HttpMethod httpMethod,
                     Integer orderNum, ResourceType resourceType) {
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        this.orderNum = orderNum;
        this.resourceType = resourceType;
    }
}
