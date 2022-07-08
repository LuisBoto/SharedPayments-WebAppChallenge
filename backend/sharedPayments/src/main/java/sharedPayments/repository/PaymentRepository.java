package sharedPayments.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import sharedPayments.model.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
