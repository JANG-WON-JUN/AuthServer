package com.my.authserver.support.repository;

import com.my.authserver.annotation.MyDataJpaTest;
import com.my.authserver.member.auth.repository.ResourceRepository;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

@MyDataJpaTest
public abstract class RepositoryTestSupport {

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ResourceRepository resourcesRepository;

}
