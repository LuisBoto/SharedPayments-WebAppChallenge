package sharedPayments.integration;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public enum QueryEnum {
	
	EMPTY_DATABASE("drop table User, Payment");
	
	private String query;
	
	private QueryEnum(String query) {
		this.query = query;
	}
	
	public void execute(Driver driver, Map<String, String> dbConfig) {
		var properties = new Properties();
		properties.put("username", dbConfig.get("username"));
		properties.put("password", dbConfig.get("password"));
		try(var connection = driver.connect(dbConfig.get("url"), properties)) {
			var preparedStatement = connection.prepareStatement(this.query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
