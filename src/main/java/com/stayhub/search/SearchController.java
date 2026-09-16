package com.stayhub.search;

import com.stayhub.common.response.ApiResponse;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.search.dto.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ApiResponse<Page<PropertySummary>> search(SearchCriteria criteria,
                                                     @PageableDefault(size = 12, sort = "createdAt") Pageable pageable) {
        return ApiResponse.success(searchService.search(criteria, pageable), "Properties retrieved successfully");
    }
}
