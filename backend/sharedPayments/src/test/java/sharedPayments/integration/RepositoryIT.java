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
		QueryEnum.RESET_HIBERNATE_AUTO_ID.execute(dbConfig);
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
	
	private void setUpUsers(User... users) {
		for (User user : users) 
			this.repoHandler.save(user);
	}
	
	@Test
	void givenNoUsers_WhenSaveNewUser_ThenUsersTableContainsThatUser() throws SQLException {
		User user = new User("Fuencisla");
		user = this.repoHandler.save(user);
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		assertThat(user.getId(), is(1L));
		assertThat(users.getString("id"), is(user.getId().toString()));
		assertThat(users.getString("name"), is(user.getName()));
	}
	
	@Test
	void givenOneUser_WhenSaveNewUser_ThenUsersTableContainsBoth() throws SQLException {
		this.setUpUsers(new User("Juana"));
		User user = new User("NewUser");
		user = this.repoHandler.save(user);
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		assertThat(users.getString("name"), is("Juana"));
		assertThat(users.next(), is(true));
		assertThat(users.getString("name"), is(user.getName()));
		assertThat(users.getString("id"), is("2"));
	}

}
