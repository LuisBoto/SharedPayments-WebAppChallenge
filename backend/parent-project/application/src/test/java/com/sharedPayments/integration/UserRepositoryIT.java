package com.sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.sharedPayments.model.User;
import com.sharedPayments.ports.UserRepository;

import jakarta.inject.Inject;

public class UserRepositoryIT extends GenericIT {

	@Inject
	private UserRepository userRepository;

	@Override
	protected long getInitialID() {
		return 100L;
	}

	private void saveUsersToRepository(User... users) {
		for (User u : users)
			this.userRepository.save(u);
	}

	//@Test
	void givenOneNewUser_WhenSaved_ThenUsersTableContainsThatUser() throws SQLException {
		User user = new User("Fuencisla");
		user = this.userRepository.save(user);

		var users = this.getAllUsersFromDB();

		assertEquals(6, users.size());
		assertThat(users.get(5).getId(), is(100L));
		assertThat(users.get(5).getId(), is(user.getId()));
		assertThat(users.get(5).getName(), is(user.getName()));
	}

	//@Test
	void givenOneNewUser_WhenSaved_ThenUsersTableContainsAll() throws SQLException {
		User user1 = new User("NewUser");
		this.saveUsersToRepository(user1);

		var users = this.getAllUsersFromDB();

		assertEquals(6, users.size());
		assertThat(users.get(4).getId(), is(5L));
		assertThat(users.get(4).getName(), is("Rebeca"));
		assertThat(users.get(5).getId(), is(user1.getId()));
		assertThat(users.get(5).getName(), is(user1.getName()));
	}

	//@Test
	void givenSeveralNewUsers_WhenSaved_ThenAutoGeneratedIdIncreasesAccordingly() throws SQLException {
		this.saveUsersToRepository(
				new User("User100"), 
				new User("User101"), 
				new User("User102"), 
				new User("User103"));

		User userWithId104 = new User("User104");
		this.userRepository.save(userWithId104);
		var users = this.getAllUsersFromDB();

		assertEquals(10, users.size());
		assertThat(users.get(5).getId(), is(100L));
		assertThat(users.get(9).getId(), is(104L));

	}

	//@Test
	void givenNoNewUsers_WhenFindAll_ThenListIsEmpty() throws SQLException {
		assertEquals(this.getAllUsersFromDB(), this.userRepository.findAll());
	}

	//@Test
	void givenSeveralNewUsers_WhenFindAll_ThenListContainsAllUsers() throws SQLException {
		User[] dbUsers = { new User("George"), new User("Martha"), new User("Bilbo") };
		this.saveUsersToRepository(dbUsers);

		assertEquals(this.getAllUsersFromDB(), this.userRepository.findAll());
	}

	//@Test
	void givenOneNewUser_WhenUpdateUserName_ThenDatabaseHasUpdatedData() throws SQLException {
		String newName = "NewName";
		this.saveUsersToRepository(new User("OldName"));

		User user = this.userRepository.findById(100L);
		user.setName(newName);
		user = this.userRepository.update(user);
		var users = this.getAllUsersFromDB();

		assertEquals(6, users.size());
		assertThat(users.get(5).getId(), is(100L));
		assertThat(users.get(5).getName(), is(newName));
	}

}
