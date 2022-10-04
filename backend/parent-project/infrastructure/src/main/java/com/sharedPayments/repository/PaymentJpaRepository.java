package com.sharedPayments.repository;

import com.sharedPayments.model.PaymentEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

}
