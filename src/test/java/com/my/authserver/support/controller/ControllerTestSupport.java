package com.my.authserver.support.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.authserver.member.auth.web.ResourceController;
import com.my.authserver.member.auth.web.RoleController;
import com.my.authserver.member.auth.web.RoleHierarchyController;
import com.my.authserver.member.web.PasswordController;

/**
 * @see ControllerTestMockBeanSupport
 */
@WebMvcTest(controllers = {
	PasswordController.class,
	RoleController.class,
	RoleHierarchyController.class,
	ResourceController.class
})
// 스프링 시큐리티를 사용하지 않을 때 필터 제외
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport extends ControllerTestMockBeanSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

}
