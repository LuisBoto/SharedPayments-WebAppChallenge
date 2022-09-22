package sharedPayments.integration;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public enum QueryEnum {
	
	EMPTY_DATABASE("drop table if exists User, Payment"),
	
	CREATE_USER_TABLE("create table User ("
			+ "id bigint not null, "
			+ "debt decimal(19,2), "
			+ "name varchar(255), "
			+ "primary key (id))"),
	
	CREATE_PAYMENT_TABLE("create table Payment ("
			+ "id bigint not null, "
			+ "description varchar(255), "
			+ "payment_date bigint, "
			+ "price decimal(19,2), "
			+ "payer_id bigint not null,"
			+ "FOREIGN KEY (payer_id) REFERENCES User(id), "
			+ "PRIMARY KEY (id));");
	
	private String query;
	
	private QueryEnum(String query) {
		this.query = query;
	}
	
	public void execute(Map<String, String> dbConfig) {		
		try(var connection = DriverManager.getConnection(
				String.format("%s?user=%s&password=%s", 
						dbConfig.get("url"), dbConfig.get("username"), dbConfig.get("password")))) {
			var preparedStatement = connection.prepareStatement(this.query);
			preparedStatement.execute();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
