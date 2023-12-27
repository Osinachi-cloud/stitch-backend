package com.stitch.user.repository;

import com.stitch.user.model.entity.ContactVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactVerificationRepository extends JpaRepository<ContactVerification, Long> {
    ContactVerification findFirstByEmailAddressOrderByDateCreatedDesc(String emailAddress);
}
