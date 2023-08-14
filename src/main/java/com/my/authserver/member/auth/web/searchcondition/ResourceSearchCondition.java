package com.my.authserver.member.auth.web.searchcondition;

import com.my.authserver.member.enums.HttpMethod;
import com.my.authserver.member.enums.ResourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@NoArgsConstructor
public class ResourceSearchCondition {

    private static final int MAX_SIZE = 2000;
    private final int size = 10;
    private int page;
    private String keyword;
    private HttpMethod httpMethod;
    private ResourceType resourceType;

    @Builder
    private ResourceSearchCondition(int page, String keyword, HttpMethod httpMethod, ResourceType resourceType) {
        this.page = page;
        this.keyword = keyword;
        this.httpMethod = httpMethod;
        this.resourceType = resourceType;
    }

    public Pageable getPageable() {
        return PageRequest.of(max(0, page), min(size, MAX_SIZE));
    }
}
