package com.example.demo.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class OpenTourSpecification {
    public static Specification<OpenTour> hasStatus(String status) {
        return (Root<OpenTour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<OpenTour> hasStartDate(LocalDate startDate) {
        return (Root<OpenTour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("startDate"), startDate);
    }

    public static Specification<OpenTour> hasPriceBetween(BigDecimal min, BigDecimal max) {
        return (Root<OpenTour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("price"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), max);
            }
            return criteriaBuilder.conjunction(); // Trường hợp không có điều kiện
        };
    }

    public static Specification<OpenTour> hasTourAndStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            Join<OpenTour, Tour> tourJoin = root.join("tour"); // ánh xạ đến thuộc tính "tour"
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("status"), status), // status của OpenTour
                    criteriaBuilder.equal(tourJoin.get("status"), "OPEN") // status của Tour
            );
        };
    }
    public static Specification<OpenTour> hasFarms(Set<String> farms) {
        return (Root<OpenTour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<OpenTour, Tour> tourJoin = root.join("tour"); // Join OpenTour -> Tour
            Join<Tour, Farm> farmJoin = tourJoin.join("farms"); // Join Tour -> Farms
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(farmJoin.get("farmName"));
            for (String farm : farms) {
                inClause.value(farm);
            }
            return inClause;
        };
    }
}
