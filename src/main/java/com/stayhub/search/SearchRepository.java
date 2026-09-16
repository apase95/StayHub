package com.stayhub.search;

import com.stayhub.property.Amenity;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import com.stayhub.search.dto.SearchCriteria;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final PropertyRepository propertyRepository;

    public Page<Property> search(SearchCriteria criteria, Pageable pageable) {
        return propertyRepository.findAll(toSpecification(criteria), pageable);
    }

    private Specification<Property> toSpecification(SearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("status"), PropertyStatus.ACTIVE));

            if (criteria != null) {
                if (hasText(criteria.getLocation())) {
                    String keyword = "%" + criteria.getLocation().trim().toLowerCase() + "%";
                    predicates.add(builder.or(
                            builder.like(builder.lower(root.get("city")), keyword),
                            builder.like(builder.lower(root.get("address")), keyword),
                            builder.like(builder.lower(root.get("title")), keyword)
                    ));
                }
                if (criteria.getGuests() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("maxGuests"), criteria.getGuests()));
                }
                if (criteria.getMinPrice() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("pricePerNight"), criteria.getMinPrice()));
                }
                if (criteria.getMaxPrice() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("pricePerNight"), criteria.getMaxPrice()));
                }
                if (criteria.getType() != null && !criteria.getType().isEmpty()) {
                    predicates.add(root.get("propertyType").in(criteria.getType()));
                }
                if (criteria.getBedrooms() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("bedrooms"), criteria.getBedrooms()));
                }
                if (criteria.getBeds() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("beds"), criteria.getBeds()));
                }
                if (criteria.getBathrooms() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("bathrooms"), criteria.getBathrooms()));
                }
                if (criteria.getRating() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("ratingAvg"), criteria.getRating()));
                }
                if (criteria.getAmenities() != null && !criteria.getAmenities().isEmpty()) {
                    Join<Property, Amenity> amenityJoin = root.join("amenities", JoinType.INNER);
                    predicates.add(amenityJoin.get("id").in(criteria.getAmenities()));
                }
            }

            query.distinct(true);
            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
