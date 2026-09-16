package com.stayhub.search;

import com.stayhub.property.dto.PropertySummary;
import com.stayhub.search.dto.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<PropertySummary> search(SearchCriteria criteria, Pageable pageable);
}
