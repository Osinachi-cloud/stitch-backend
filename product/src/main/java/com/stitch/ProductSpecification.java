package com.stitch;

import com.stitch.model.ProductCategory;
import com.stitch.model.entity.Product;
import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public final class ProductSpecification {

    public static Specification<Product> onSpecificInstant(Instant specificInstant) {
        return (root, query, builder) -> {
            if (specificInstant != null) {
                Instant startOfDay = specificInstant.truncatedTo(java.time.temporal.ChronoUnit.DAYS);
                Instant endOfDay = startOfDay.plus(java.time.Duration.ofDays(1));
                return builder.between(root.get("dateCreated"), startOfDay, endOfDay);
            }
            return null;
        };
    }

    public static Specification<Product> betweenInstants(Instant startDate, Instant endDate) {
        return (root, query, builder) -> {
            if (startDate != null && endDate != null) {
                return builder.between(root.get("dateCreated"), startDate, endDate);
            }
            return null;
        };
    }



    public static Specification<Product> nameEqual(String name) {
        return (root, query, builder) ->
                name != null ? builder.equal(root.get("name"), name) : null;
    }

    public static Specification<Product> productIdEqual(String productId) {
        return (root, query, builder) ->
                productId != null ? builder.equal(root.get("productId"), productId) : null;
    }

    public static Specification<Product> vendorEqual(UserEntity vendor) {
        return (root, query, builder) ->
                vendor != null ? builder.equal(root.get("userEntity"), vendor) : null;
    }

    public static Specification<Product> codeEqual(String code) {
        return (root, query, builder) ->
                code != null ? builder.equal(root.get("code"), code) : null;
    }

    public static Specification<Product> categoryEqual(ProductCategory category) {
        return (root, query, builder) ->
                category != null ? builder.equal(root.get("category"), category) : null;
    }
}

