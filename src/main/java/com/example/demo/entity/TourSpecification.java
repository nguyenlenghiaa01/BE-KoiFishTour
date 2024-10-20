package com.example.demo.entity;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class TourSpecification {

    public static Specification<Tour> hasStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
        };
    }

    public static Specification<Tour> hasDuration(String duration) {
        return (root, query, criteriaBuilder) -> {
            if (duration == null || duration.isEmpty()) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("duration"), duration);
        };
    }

    public static Specification<Tour> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.join("openTours").get("totalPrice"), maxPrice);
        };
    }


    public static Specification<Tour> hasFarms(Set<String> farmNames) {
        return (root, query, criteriaBuilder) -> {
            if (farmNames == null || farmNames.isEmpty()) return criteriaBuilder.conjunction();
            return root.join("farms").get("name").in(farmNames);
        };
    }
    public static Specification<Tour> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
