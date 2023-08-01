package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE_HIERARCHY_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleHierarchy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Role parent;

	@OneToOne
	private Role child;

	@Builder
	private RoleHierarchy(Role parent, Role child) {
		this.parent = parent;
		this.child = child;
	}

	public static RoleHierarchy create(Role parent, Role child) {
		return RoleHierarchy.builder()
			.parent(parent)
			.child(child)
			.build();
	}
}
