package sharedPayments.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest(transactional = false)
public abstract class GenericIT {
	
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
		QueryEnum.DELETE_INSERTED_PAYMENTS.executeFormatted(dbConfig, this.getInitialID());
		QueryEnum.DELETE_INSERTED_USERS.executeFormatted(dbConfig, this.getInitialID());
		QueryEnum.UPDATE_HIBERNATE_AUTO_ID.executeFormatted(dbConfig, this.getInitialID()); 
	}
	
	private void setUpDatabaseConfig() {
		dbConfig.put("url", url);
		dbConfig.put("username", username);
		dbConfig.put("password", password);
	}
	
	protected abstract long getInitialID();

}
