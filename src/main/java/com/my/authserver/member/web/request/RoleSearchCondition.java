package com.my.authserver.member.web.request;

import static java.lang.Math.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleSearchCondition {

	private static final int MAX_SIZE = 2000;
	private final int size = 10;
	private int page;
	private String keyword;

	@Builder
	private RoleSearchCondition(int page, String keyword) {
		this.page = page;
		this.keyword = keyword;
	}

	public Pageable getPageable() {
		return PageRequest.of(max(0, page), min(size, MAX_SIZE));
	}
}
