package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE_RESOURCES_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleResource extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resource_id")
	private Resource resource;

	public static RoleResource create(Role role, Resource resource) {
		return RoleResource.builder()
			.role(role)
			.resource(resource)
			.build();
	}

	@Builder
	private RoleResource(Role role, Resource resource) {
		this.role = role;
		this.resource = resource;
	}
}
