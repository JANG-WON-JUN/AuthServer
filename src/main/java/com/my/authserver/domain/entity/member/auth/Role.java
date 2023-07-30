package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.member.enums.Roles;

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

@Entity
@Getter
@Table(name = "ROLE_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(20)")
	private Roles roleName;

	private String roleDesc; // 권한 상세설명

	@Builder
	public Role(Roles roleName, String roleDesc) {
		this.roleName = roleName;
		this.roleDesc = roleDesc;
	}
}
