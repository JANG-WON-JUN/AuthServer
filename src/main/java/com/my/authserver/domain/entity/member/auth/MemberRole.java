package com.my.authserver.domain.entity.member.auth;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.auth.embedded.MemberRoleId;
import com.my.authserver.member.enums.RoleType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER_ROLES_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRole extends BaseEntity {

    @EmbeddedId
    private MemberRoleId id;

    @Builder
    public MemberRole(Member member, Role role) {
        id = MemberRoleId.builder()
                .member(member)
                .role(role)
                .build();
    }

    public RoleType getMemberRole() {
        return id.getRole().getRoleType();
    }
}
