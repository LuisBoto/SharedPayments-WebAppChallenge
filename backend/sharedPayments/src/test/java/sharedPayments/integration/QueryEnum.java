package sharedPayments.integration;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.transaction.Transactional;

public enum QueryEnum {
		
	UPDATE_HIBERNATE_AUTO_ID("update hibernate_sequence set next_val=%d"),
	
	DELETE_INSERTED_PAYMENTS("delete from payment where id>=100"),
	
	DELETE_INSERTED_USERS("delete from user where id>=100"),
	
	SELECT_ALL_USERS("select * from user"),
	
	SELECT_ALL_PAYMENTS("select * from payment");
	
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
