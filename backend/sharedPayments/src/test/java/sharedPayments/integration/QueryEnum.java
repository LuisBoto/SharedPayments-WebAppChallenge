package sharedPayments.integration;

import java.sql.SQLException;
import java.util.Map;

import org.testcontainers.containers.MySQLContainer;

public enum QueryEnum {
	
	EMPTY_DATABASE("drop table if exists User, Payment");
	
	private String query;
	
	private QueryEnum(String query) {
		this.query = query;
	}
	
	public void execute(MySQLContainer<?> mySQL, Map<String, String> dbConfig) {		
		try(var connection = mySQL.createConnection(
				String.format("?user=?s&password=?s", dbConfig.get("username"), dbConfig.get("password")))) {
			var preparedStatement = connection.prepareStatement(this.query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
