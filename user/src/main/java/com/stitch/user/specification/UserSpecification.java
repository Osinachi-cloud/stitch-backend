package com.stitch.user.specification;

import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
public final class UserSpecification {

    public static Specification<UserEntity> onSpecificInstant(Instant specificInstant) {
        return (root, query, builder) -> {
            if (specificInstant != null) {
                Instant startOfDay = specificInstant.truncatedTo(java.time.temporal.ChronoUnit.DAYS);
                Instant endOfDay = startOfDay.plus(java.time.Duration.ofDays(1));
                return builder.between(root.get("dateCreated"), startOfDay, endOfDay);
            }
            return null;
        };
    }

    public static Specification<UserEntity> betweenInstants(Instant startDate, Instant endDate) {
        return (root, query, builder) -> {
            if (startDate != null && endDate != null) {
                return builder.between(root.get("dateCreated"), startDate, endDate);
            }
            return null;
        };
    }



    public static Specification<UserEntity> firstNameEqual(String firstName) {
        return (root, query, builder) ->
                firstName != null ? builder.equal(root.get("firstName"), firstName) : null;
    }

    public static Specification<UserEntity> lastNameEqual(String lastName) {
        return (root, query, builder) ->
                lastName != null ? builder.equal(root.get("lastName"), lastName) : null;
    }

    public static Specification<UserEntity> roleIdEqual(Long roleId) {
        return (root, query, builder) ->
                roleId != null ? builder.equal(root.get("role").get("id"), roleId) : null;
    }

    public static Specification<UserEntity> emailEqual(String emailAddress) {
        return (root, query, builder) ->
                emailAddress != null ? builder.equal(root.get("emailAddress"), emailAddress) : null;
    }

}

