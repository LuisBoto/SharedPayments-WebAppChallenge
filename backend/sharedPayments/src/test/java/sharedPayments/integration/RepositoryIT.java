package sharedPayments.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public abstract class RepositoryIT {
	
	protected static long currentId = 1L;

	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;

	static Map<String, String> dbConfig = new HashMap<String, String>();

	@Inject
	RepositoryHandler repoHandler;

	@BeforeEach
	void resetDB() {
		if (dbConfig.isEmpty())
			this.setUpDatabaseConfig();
		QueryEnum.EMPTY_DATABASE.execute(dbConfig);
		this.resetId();
		QueryEnum.CREATE_USER_TABLE.execute(dbConfig);
		QueryEnum.CREATE_PAYMENT_TABLE.execute(dbConfig);
	}
	
	private void setUpDatabaseConfig() {
		dbConfig.put("url", url);
		dbConfig.put("username", username);
		dbConfig.put("password", password);
	}
	
	private void resetId() {
		currentId = 1L;
		QueryEnum.RESET_HIBERNATE_AUTO_ID.execute(dbConfig);
	}
	
	public static void incrementCurrentId() {
		RepositoryIT.currentId++;
		QueryEnum.UPDATE_HIBERNATE_AUTO_ID.executeFormatted(dbConfig, RepositoryIT.currentId); 
	}

}
