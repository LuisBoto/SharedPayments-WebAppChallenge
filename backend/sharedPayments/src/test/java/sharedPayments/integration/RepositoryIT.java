package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.repository.UserRepository;

@MicronautTest
public class RepositoryIT {
	
	//@Inject
	//ApplicationContext appContext;
	
	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;
	private Map<String, String> dbConfig = new HashMap<String, String>();
	
	@Inject
	private UserRepository userRepository;
	private PaymentRepository paymentRepository;
	
	public RepositoryIT() {
	}

	@BeforeEach
	void resetDB() {
		//this.userRepository = ApplicationContext.run("dev").findBean(UserRepository.class).get();
		//this.userRepository = appContext.createBean(UserRepository.class);
		this.dbConfig.put("url", url);
		this.dbConfig.put("username", username);
		this.dbConfig.put("password", password);
		QueryEnum.EMPTY_DATABASE.execute(dbConfig);
		QueryEnum.CREATE_USER_TABLE.execute(dbConfig);
		QueryEnum.CREATE_PAYMENT_TABLE.execute(dbConfig);
	}
	
	@Test
	void notImplemented() {
		//System.out.println(this.userRepository.save(new User("Fuencisla")));
		assertThat(1, is(1));
	}

}
