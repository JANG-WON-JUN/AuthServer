package com.my.authserver.member.auth.web.searchcondition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
