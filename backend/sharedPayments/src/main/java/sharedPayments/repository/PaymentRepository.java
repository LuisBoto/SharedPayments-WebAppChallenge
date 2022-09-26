package sharedPayments.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import sharedPayments.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
