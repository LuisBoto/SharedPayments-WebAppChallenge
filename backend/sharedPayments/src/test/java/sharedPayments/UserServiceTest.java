package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import sharedPayments.model.User;
import sharedPayments.model.dto.UserDto;
import sharedPayments.repository.UserRepository;
import sharedPayments.service.UserService;

public class UserServiceTest {
	
	private UserRepository userRepository = mock(UserRepository.class);
	private UserService userService = new UserService(this.userRepository);
	
	private void setUpMockRepositoryUsersWithDebts(Double... debts) {
		List<User> users = new ArrayList<User>();
		for (int i=0; i<debts.length; i++) {
			User user = new User("Name");
			user.setDebt(debts[i]);
			user.setId((long) i);
			users.add(user);
		}
		
		when(this.userRepository.findAll()).thenReturn(users);
		when(this.userRepository.count()).thenReturn((long) users.size());
		when(this.userRepository.save(any(User.class))).thenAnswer(
				call -> {
					User paramUser = call.getArgument(0);
					for (User u : users) 
						if (u.getId() == paramUser.getId()) {
							u.setDebt(paramUser.getDebt());
							return u;
						}
					users.add(paramUser);
					return paramUser;
				});
	}
	
	@Test
	void givenTwoPayments_WhenGetAllPayments_ThenSizeIsTwo() {
		when(this.userRepository.findAll()).thenReturn( Arrays.asList(
				new User(),
				new User()
				));

		assertTrue(this.userService.getUsers().size() == 2);
	}
	
	@Test
	void givenNoUsers_WhenUpdateUserDebts_ThenSizeIsZero() {
		this.setUpMockRepositoryUsersWithDebts();
		this.userService.updateUserDebts(1L, 100D);
		verify(this.userRepository, times(1)).count();
		
	}
	
	@Test
	void givenNewValidUser_WhenCreateUser_ThenUserIsSaved() {
		UserDto userDto = new UserDto(2L, "Marcos", 50);

		User user = new User();
		user.setId(2L);
		when(this.userRepository.save(any(User.class))).then(call -> {
			User result = userDto.toEntity();
			result.setId(2L);
			return result;
		});
		
		UserDto createdUser = this.userService.createUser(userDto);
		assertTrue(userDto.equals(createdUser));
	}

}
