package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import sharedPayments.model.User;

@MicronautTest
public class RepositoryIT {
	
	@Value("${datasources.default.username}")
	private String username;
	@Value("${datasources.default.password}")
	private String password;
	@Value("${datasources.default.url}")
	private String url;
	
	private Map<String, String> dbConfig = new HashMap<String, String>();
	
	@Inject
	private RepositoryHandler repoHandler; 

	@BeforeEach
	void resetDB() {
		this.setUpDatabaseConfig();
		QueryEnum.EMPTY_DATABASE.execute(dbConfig);
		QueryEnum.CREATE_USER_TABLE.execute(dbConfig);
		QueryEnum.CREATE_PAYMENT_TABLE.execute(dbConfig);
	}
	
	private void setUpDatabaseConfig() {
		if (this.dbConfig.isEmpty()) {
			this.dbConfig.put("url", url);
			this.dbConfig.put("username", username);
			this.dbConfig.put("password", password);
		}
	}
	
	@Test
	void givenNoUsers_WhenSaveNewUser_ThenUsersTableContainsThatUser() throws InterruptedException, SQLException {
		User user = new User("Fuencisla");
		user = this.repoHandler.save(user);
		assertThat(user.getName(), is("Fuencisla"));
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		assertThat(users.getString("name"), is(user.getName()));
	}

}
