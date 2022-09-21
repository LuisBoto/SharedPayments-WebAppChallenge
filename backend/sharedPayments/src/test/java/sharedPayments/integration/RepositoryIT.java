package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;



@Testcontainers
public class RepositoryIT {
	
	@Container
	private static MySQLContainer<?> mySQL = new MySQLContainer<>(DockerImageName.parse("mysql:8"))
			.withExposedPorts(3306)
			.withEnv("MYSQL_ROOT_PASSWORD", "password")
			.withEnv("MYSQL_DATABASE", "test")
			.withEnv("MYSQL_USER", "admin")
			.withEnv("MYSQL_PASSWORD", "password");
	
	private Map<String, String> dbConfig = new HashMap<String, String>();
	
	@BeforeAll
	static void setUpDBContainer() throws NamingException {
		mySQL.start();
	}
	
	@BeforeEach
	void resetDB() {
		this.dbConfig.put("url", mySQL.getJdbcUrl());
		this.dbConfig.put("username", mySQL.getUsername());
		this.dbConfig.put("password", mySQL.getPassword());
		QueryEnum.EMPTY_DATABASE.execute(mySQL.getJdbcDriverInstance(), dbConfig);
	}
	
	@AfterAll
	static void closeDBContainer() {
		mySQL.close();
		mySQL.stop();
	}

	@Test
	void notImplemented() {
		assertThat(1, is(1));
	}

}
