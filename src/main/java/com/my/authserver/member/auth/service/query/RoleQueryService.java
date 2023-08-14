package com.my.authserver.member.auth.service.query;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.common.web.exception.RoleNotFound;
import com.my.authserver.domain.entity.member.auth.Role;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.web.searchcondition.RoleSearchCondition;
import com.my.authserver.member.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleQueryService {

    private final MessageSourceUtils messageSourceUtils;
    private final RoleRepository roleRepository;

    public Role findByRoleType(RoleType roleType) {
        return getRoleFrom(roleRepository.findByRoleType(roleType));
    }

    public Role findById(Long id) {
        return getRoleFrom(roleRepository.findById(id));
    }

    public Page<Role> findRolesWithCondition(RoleSearchCondition condition) {
        return roleRepository.findRolesWithCondition(condition);
    }

    private Role getRoleFrom(Optional<Role> roleOptional) {
        return roleOptional.orElseThrow(()
                -> new RoleNotFound(messageSourceUtils.getMessage("error.noRole")));
    }

    public boolean exists(RoleType roleType) {
        return roleRepository.findByRoleType(roleType).isPresent();
    }
}
