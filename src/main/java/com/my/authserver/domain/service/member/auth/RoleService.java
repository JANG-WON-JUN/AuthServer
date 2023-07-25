package com.my.authserver.domain.service.member.auth;

import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.domain.enums.Roles;
import com.my.authserver.domain.repository.member.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void createRole(Role role){
        roleRepository.save(role);
    }

    public Role findByRoleName(Roles role){
        return roleRepository.findByRoleName(role);
    }
}
