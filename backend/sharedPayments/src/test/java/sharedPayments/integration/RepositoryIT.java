package sharedPayments.integration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import sharedPayments.model.User;

@MicronautTest
public class RepositoryIT {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;
	
	private Map<String, String> dbConfig = new HashMap<String, String>();
	
	@Inject
	private RepositoryHandler userRepository; 

	@BeforeEach
	void resetDB() {
		this.dbConfig.put("url", url);
		this.dbConfig.put("username", username);
		this.dbConfig.put("password", password);
		//QueryEnum.EMPTY_DATABASE.execute(dbConfig);
		//QueryEnum.CREATE_USER_TABLE.execute(dbConfig);
		//QueryEnum.CREATE_PAYMENT_TABLE.execute(dbConfig);
	}
	
	@Test
	void givenNoUsers_WhenSaveNewUser_ThenUsersTableHasOneUser() throws InterruptedException, SQLException {

		//sessionFactory.getCurrentSession().setHibernateFlushMode(FlushMode.MANUAL);
		User user = new User("Fuencisla");
		this.userRepository.findAll().forEach(System.out::println);
		user = this.userRepository.save(user);
		user = this.userRepository.save(new User("Pepita"));
		sessionFactory.getCurrentSession().flush();
		//assertThat(user.getId(), is(1L));
		//assertThat(user.getName(), is("Fuencisla"));
		this.userRepository.findAll().forEach(System.out::println);
		
		
		//CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		//assertThat(users.getString("name"), is(user.getName()));
	}

}
