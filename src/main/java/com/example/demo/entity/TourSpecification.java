package com.example.demo.entity;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class TourSpecification {

    // Specification for startDate
    public static Specification<Tour> hasStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("startDate"), startDate);
    }

    // Specification for duration
    public static Specification<Tour> hasDuration(String duration) {
        return (root, query, criteriaBuilder) -> {
            if (duration == null || duration.isEmpty()) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("duration"), duration);
        };
    }

    // Specification for max price (BigDecimal)
    public static Specification<Tour> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    // Specification for price range (BigDecimal)
    public static Specification<Tour> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            } else if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }

    // Specification for farms
    public static Specification<Tour> hasFarms(Set<String> farmSet) {
        return (root, query, criteriaBuilder) -> {
            if (farmSet == null || farmSet.isEmpty()) return criteriaBuilder.conjunction();
            Join<Tour, Farm> farmsJoin = root.join("farms");
            return farmsJoin.get("farmName").in(farmSet);
        };
    }

    // Specification for koiFish types
    public static Specification<Tour> hasKoiFishType(Set<String> koiFishTypes) {
        return (root, query, criteriaBuilder) -> {
            if (koiFishTypes == null || koiFishTypes.isEmpty()) return criteriaBuilder.conjunction();
            Join<Tour, KoiFish> koiFishJoin = root.join("koiFish");
            return koiFishJoin.get("type").in(koiFishTypes);
        };
    }

    // Specification for status
    public static Specification<Tour> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // Specification for duration in days (int)
    public static Specification<Tour> hasDurationDays(int days) {
        return (root, query, criteriaBuilder) -> {
            if (days <= 0) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("duration"), days + " days");
        };
    }
}
