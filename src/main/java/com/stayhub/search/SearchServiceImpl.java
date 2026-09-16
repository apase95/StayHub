package com.stayhub.search;

import com.stayhub.property.PropertyMapper;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.search.dto.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;
    private final PropertyMapper propertyMapper;

    @Override
    public Page<PropertySummary> search(SearchCriteria criteria, Pageable pageable) {
        return searchRepository.search(criteria, pageable)
                .map(propertyMapper::toSummary);
    }
}
