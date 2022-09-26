package sharedPayments.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public abstract class RepositoryIT {

	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;

	Map<String, String> dbConfig = new HashMap<String, String>();

	@Inject
	RepositoryHandler repoHandler;

	@BeforeEach
	void resetDB() {
		if (this.dbConfig.isEmpty())
			this.setUpDatabaseConfig();
		QueryEnum.EMPTY_DATABASE.execute(dbConfig);
		QueryEnum.RESET_HIBERNATE_AUTO_ID.execute(dbConfig);
		QueryEnum.CREATE_USER_TABLE.execute(dbConfig);
		QueryEnum.CREATE_PAYMENT_TABLE.execute(dbConfig);
	}

	private void setUpDatabaseConfig() {
		this.dbConfig.put("url", url);
		this.dbConfig.put("username", username);
		this.dbConfig.put("password", password);
	}

}
