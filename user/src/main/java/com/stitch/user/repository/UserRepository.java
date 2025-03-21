package com.stitch.user.repository;

import com.stitch.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends RevisionRepository<UserEntity, Long, Long>, JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    boolean existsByEmailAddress(String emailAddress);

    Optional<UserEntity> findByEmailAddress(String emailAddress);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findByUserId(String customerId);
}
