package com.stitch.user.repository;

import com.stitch.user.model.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    PasswordReset findFirstByEmailAddressOrderByDateCreatedDesc(String emailAddress);
}
