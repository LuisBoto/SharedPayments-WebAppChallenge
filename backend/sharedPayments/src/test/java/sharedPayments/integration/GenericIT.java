package sharedPayments.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import sharedPayments.model.Payment;
import sharedPayments.model.User;

@MicronautTest
public abstract class GenericIT {
	
	@Inject
	protected EntityManager entityManager;
	
	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;

	static Map<String, String> dbConfig = new HashMap<String, String>();
	
	@BeforeEach
	void resetDB() {
		if (dbConfig.isEmpty())
			this.setUpDatabaseConfig();
		this.entityManager.createNativeQuery(
				String.format("update hibernate_sequence set next_val=%d", this.getInitialID()))
		.executeUpdate();
	}
	
	private void setUpDatabaseConfig() {
		dbConfig.put("url", url);
		dbConfig.put("username", username);
		dbConfig.put("password", password);
	}
	
	@SuppressWarnings("unchecked")
	protected List<User> getAllUsersFromDB() {
		return this.entityManager.createQuery("from User").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<Payment> getAllPaymentsFromDB() {
		return this.entityManager.createQuery("from Payment").getResultList();
	}
	
	protected abstract long getInitialID();

}
