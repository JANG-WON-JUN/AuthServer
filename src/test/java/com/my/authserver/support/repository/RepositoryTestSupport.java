package com.my.authserver.support.repository;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.authserver.annotation.MyDataJpaTest;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.repository.MemberRepository;

@MyDataJpaTest
public abstract class RepositoryTestSupport {

	@Autowired
	protected RoleRepository roleRepository;

	@Autowired
	protected MemberRepository memberRepository;
}
