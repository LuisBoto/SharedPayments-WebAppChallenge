package sharedPayments.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import sharedPayments.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
