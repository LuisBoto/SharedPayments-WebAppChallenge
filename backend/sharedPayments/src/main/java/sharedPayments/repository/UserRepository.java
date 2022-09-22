package sharedPayments.repository;

import io.micronaut.context.annotation.Requires;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import sharedPayments.model.User;

@Repository
@Requires(env = {"dev", "prod"})
public interface UserRepository extends CrudRepository<User, Long> {

}
