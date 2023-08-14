package com.my.authserver.member.auth.web.searchcondition;

import static java.lang.Math.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.my.authserver.member.enums.RoleType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleResourceSearchCondition {

	private static final int MAX_SIZE = 2000;
	private final int size = 10;
	private int page;
	private List<RoleType> roleTypes;

	@Builder
	private RoleResourceSearchCondition(int page, List<RoleType> roleTypes) {
		this.page = page;
		this.roleTypes = roleTypes;
	}

	public Pageable getPageable() {
		return PageRequest.of(max(0, page), min(size, MAX_SIZE));
	}
}
