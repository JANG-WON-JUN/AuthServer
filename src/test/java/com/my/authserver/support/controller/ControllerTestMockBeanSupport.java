package com.my.authserver.support.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.authserver.common.utils.MessageSourceUtils;
import com.my.authserver.member.auth.service.ResourceService;
import com.my.authserver.member.auth.service.RoleHierarchyService;
import com.my.authserver.member.auth.service.RoleService;
import com.my.authserver.member.auth.service.query.ResourceQueryService;
import com.my.authserver.member.auth.service.query.RoleHierarchyQueryService;
import com.my.authserver.member.auth.service.query.RoleQueryService;
import com.my.authserver.member.service.PasswordService;
import com.my.authserver.member.service.query.PasswordQueryService;

/**
 * ControllerTestMockBeanSupport를 상속받아
 * 다양한 ControllerTestSupport를 구성할 수 있다.
 * 예를들어 Spring Security를 사용하는 ControllerTestSupport와
 * Spring Security를 사용하지 않는 ControllerTestSupport에서 상속받아 사용한다.
 * @see ControllerTestSupport
 */
public abstract class ControllerTestMockBeanSupport {

	@MockBean
	protected PasswordEncoder passwordEncoder;

	@MockBean
	protected MessageSourceUtils messageSourceUtils;

	@MockBean
	protected PasswordService passwordService;

	@MockBean
	protected PasswordQueryService passwordQueryService;

	@MockBean
	protected RoleService roleService;

	@MockBean
	protected RoleQueryService roleQueryService;

	@MockBean
	protected RoleHierarchyService roleHierarchyService;

	@MockBean
	protected RoleHierarchyQueryService roleHierarchyQueryService;

	@MockBean
	protected ResourceService resourceService;

	@MockBean
	protected ResourceQueryService resourceQueryService;
}
