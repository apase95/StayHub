package com.stayhub.property;

import com.stayhub.search.SearchService;
import com.stayhub.search.dto.SearchCriteria;
import com.stayhub.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final SearchService searchService;
    private final ReviewService reviewService;

    @GetMapping("/properties")
    public String search(SearchCriteria criteria,
                         @PageableDefault(size = 12, sort = "createdAt") Pageable pageable,
                         Model model) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("properties", searchService.search(criteria, pageable));
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("amenities", propertyService.getAmenities());
        return "property/search-results";
    }

    @GetMapping("/properties/{id}")
    public String showProperty(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.getPublicProperty(id));
        model.addAttribute("reviews", reviewService.getPropertyReviews(id));
        return "property/property-detail";
    }
}
