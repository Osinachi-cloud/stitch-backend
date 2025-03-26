package com.stitch.gateway.specifications;

import com.stitch.model.entity.ProductOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ProductOrderSpecifications {

    public static Specification<ProductOrder> filterOrders(Long productId, String emailAddress, String status, Long orderId, String productCategoryName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productId != null) {
                predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
            }
            if (emailAddress != null) {
                predicates.add(criteriaBuilder.equal(root.get("emailAddress"), emailAddress));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (orderId != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderId"), orderId));
            }
            if (productCategoryName != null) {
                predicates.add(criteriaBuilder.equal(root.get("productCategoryName"), productCategoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
