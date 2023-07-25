package com.my.authserver.domain.service.member.auth;

import com.my.authserver.domain.entity.member.Member;
import com.my.authserver.domain.entity.member.auth.MemberRoles;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.repository.member.auth.MemberRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberRolesService {

    private final MemberRolesRepository memberRolesRepository;

    public MemberRoles createMemberRoles(Member member, Role role){
        MemberRoles memberRoles = MemberRoles.builder()
                .member(member)
                .role(role)
                .build();

        return memberRolesRepository.save(memberRoles);
    }
}
