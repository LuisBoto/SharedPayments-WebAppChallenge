package sharedPayments.integration;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.transaction.Transactional;

public enum QueryEnum {
	
	RESET_HIBERNATE_AUTO_ID("update hibernate_sequence set next_val=1"),
	
	UPDATE_HIBERNATE_AUTO_ID("update hibernate_sequence set next_val=%d"),
	
	EMPTY_DATABASE("drop table if exists user, payment"),
	
	CREATE_USER_TABLE("create table user ("
			+ "id bigint not null, "
			+ "debt decimal(19,2), "
			+ "name varchar(255), "
			+ "primary key (id))"),
	
	CREATE_PAYMENT_TABLE("create table payment ("
			+ "id bigint not null, "
			+ "description varchar(255), "
			+ "payment_date bigint, "
			+ "price decimal(19,2), "
			+ "payer_id bigint not null,"
			+ "FOREIGN KEY (payer_id) REFERENCES user(id), "
			+ "PRIMARY KEY (id));"),
	
	SELECT_ALL_USERS("select * from user"),
	
	SELECT_ALL_PAYMENTS("select * from payment"),
	
	INSERT_INTO_USERS("insert into user (id, debt, name) values (%d, %s, '%s')") {
		@Override
		public void executeFormatted(Map<String, String> dbConfig, Object... params) {
			super.executeFormatted(dbConfig, params);
			RepositoryIT.incrementCurrentId();
		}
	},
	
	INSERT_INTO_PAYMENTS(
			"insert into payment (id, description, payment_date, price, payer_id) "
			+ "values (%d, '%s', %d, %s, %d)") {
		@Override
		public void executeFormatted(Map<String, String> dbConfig, Object... params) {
			super.executeFormatted(dbConfig, params);
			RepositoryIT.incrementCurrentId();
		}
	};
	
	private String query;
	
	private QueryEnum(String query) {
		this.query = query;
	}
	
	@Transactional
	public CachedRowSet execute(Map<String, String> dbConfig) {
		try(var connection = DriverManager.getConnection(
				String.format("%s?user=%s&password=%s", 
						dbConfig.get("url"), dbConfig.get("username"), dbConfig.get("password")));
			PreparedStatement preparedStatement = connection.prepareStatement(this.query)) {
			
			CachedRowSet rowset = RowSetProvider.newFactory().createCachedRowSet();
			System.out.println(String.format("Executing query %s", this.name()));
			preparedStatement.execute();
			var result = preparedStatement.getResultSet();
			if (result != null) {
				rowset.populate(result); 
				rowset.first();
			}
			return rowset;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public void executeFormatted(Map<String, String> dbConfig, Object... params) {
		final String baseQuery = this.query;
		this.query = String.format(this.query, params);
		this.execute(dbConfig);
		this.query = baseQuery;
	}

}
