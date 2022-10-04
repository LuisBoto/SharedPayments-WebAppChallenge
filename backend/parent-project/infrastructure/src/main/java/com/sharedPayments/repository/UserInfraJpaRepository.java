package com.sharedPayments.repository;

import com.sharedPayments.model.UserEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface UserInfraJpaRepository extends JpaRepository<UserEntity, Long> {

}
