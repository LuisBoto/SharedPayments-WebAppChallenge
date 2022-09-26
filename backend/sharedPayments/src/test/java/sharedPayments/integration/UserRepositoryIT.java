package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.junit.jupiter.api.Test;

import sharedPayments.model.User;

public class UserRepositoryIT extends RepositoryIT {
	
	private void saveDatabaseUsers(User... users) {
		for (User user : users)  {
			//user.setId(RepositoryIT.currentId);
			//QueryEnum.INSERT_INTO_USERS.execute(dbConfig, user);
			this.repoHandler.save(user);
		}
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
		User user1 = new User("OldUser");
		this.saveDatabaseUsers(user1);
		
		User user2 = new User("NewUser");
		user2 = this.repoHandler.save(user2);
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		assertThat(users.getString("name"), is(user1.getName()));
		assertThat(users.next(), is(true));
		assertThat(users.getString("name"), is(user2.getName()));
		assertThat(users.getString("id"), is("2"));
	}
	
	@Test
	void givenNoUsers_WhenSaveSeveralUsers_ThenAutoGeneratedIdIncreasesAccordingly() throws SQLException {
		User[] dbUsers = {
				new User("User1"),
				new User("User2"),
				new User("User3"),
				new User("User4")};
		for (User user : dbUsers) this.repoHandler.save(user);
		User userWithId5 = new User("User5");
		
		this.repoHandler.save(userWithId5);
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		assertThat(users.getString("id"), is("1"));
		for (int i=0; i<4; i++)
			assertTrue(users.next());
		assertThat(users.getString("id"), is("5"));
		
	}
	
	@Test
	void givenNoUsers_WhenFindAll_ThenListIsEmpty() {
		assertThat(this.repoHandler.findAllUsers().size(), is(0));
	}
	
	@Test
	void givenSeveralUsers_WhenFindAll_ThenListContainsAllUsers() {
		User[] dbUsers = {
				new User("George"),
				new User("Martha"),
				new User("Bilbo")};
		this.saveDatabaseUsers(dbUsers);
		List<User> users = this.repoHandler.findAllUsers();
		
		assertThat(users.get(0).getName(), is(dbUsers[0].getName()));
		assertThat(users.get(1).getName(), is(dbUsers[1].getName()));
		assertThat(users.get(2).getName(), is(dbUsers[2].getName()));
	}
	
	@Test
	void givenOneUser_WhenUpdateUserName_ThenDatabaseHasNewData() throws SQLException {
		String newName = "NewName";
		User user = new User("OldName");
		this.repoHandler.save(user);
		
		user = this.repoHandler.findUserById(1L).get();
		user.setName(newName);
		user = this.repoHandler.update(user);
		CachedRowSet users = QueryEnum.SELECT_ALL_USERS.execute(dbConfig);
		
		assertThat(users.getString("id"), is("1"));
		assertThat(users.getString("name"), is(newName));
	}

}
