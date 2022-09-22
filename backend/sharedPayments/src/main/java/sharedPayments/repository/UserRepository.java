package sharedPayments.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import sharedPayments.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
