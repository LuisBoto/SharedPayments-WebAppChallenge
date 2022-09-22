package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import sharedPayments.model.User;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.repository.UserRepository;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryIT {

	@Value("${datasources.mysql.username}")
	private static String username;
	@Value("${datasources.*.password}")
	private static String password;
	@Value("${datasources.default.url}")
	private static String url;
	private Map<String, String> dbConfig = new HashMap<String, String>();

	
	private UserRepository userRepository;
	
	private PaymentRepository paymentRepository;

	public RepositoryIT() {
		ApplicationContext.run("test");
	}

	@BeforeEach
	void resetDB() {
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
