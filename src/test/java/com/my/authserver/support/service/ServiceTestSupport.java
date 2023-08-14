package com.my.authserver.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.authserver.annotation.MyServiceTest;
import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.member.auth.repository.ResourceRepository;
import com.my.authserver.member.auth.repository.RoleHierarchyRepository;
import com.my.authserver.member.auth.repository.RoleRepository;
import com.my.authserver.member.auth.service.ResourceService;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.ResourceQueryService;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.service.PasswordService;
import com.my.authserver.member.service.query.PasswordQueryService;

@MyServiceTest
public abstract class ServiceTestSupport {

	@Autowired
	protected MessageSourceUtils messageSourceUtils;

	@Autowired
	protected RoleService roleService;

	@Autowired
	protected RoleQueryService roleQueryService;

	@Autowired
	protected RoleRepository roleRepository;

	@Autowired
	protected RoleHierarchyService roleHierarchyService;

	@Autowired
	protected RoleHierarchyQueryService roleHierarchyQueryService;

	@Autowired
	protected RoleHierarchyRepository roleHierarchyRepository;

	@Autowired
	protected PasswordService passwordService;

	@Autowired
	protected PasswordQueryService passwordQueryService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected ResourceService resourceService;

	@Autowired
	protected ResourceQueryService resourceQueryService;

	@Autowired
	protected ResourceRepository resourcesRepository;

}
