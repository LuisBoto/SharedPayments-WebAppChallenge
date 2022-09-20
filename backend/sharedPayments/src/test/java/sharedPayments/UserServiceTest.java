package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
			user.setId((long) i+1);
			users.add(user);
		}
		
		when(this.userRepository.findAll()).thenReturn(users);
		when(this.userRepository.findById(any(Long.class))).thenAnswer(call -> {
			return Optional.of(users.get(((Long) call.getArgument(0)).intValue()-1));
		});
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
	void givenTwoUsers_WhenGetAllUsers_ThenSizeIsTwo() {
		when(this.userRepository.findAll()).thenReturn( Arrays.asList(
				new User(),
				new User()
				));

		assertTrue(this.userService.getUsers().size() == 2);
	}
	
	@Test
	void givenNewValidUser_WhenCreateUser_ThenUserIsSaved() {
		UserDto userDto = new UserDto(2L, "Marcos", 50);

		when(this.userRepository.save(any(User.class))).then(call -> {
			User result = userDto.toEntity();
			result.setId(2L);
			return result;
		});
		
		UserDto createdUser = this.userService.createUser(userDto);
		assertTrue(userDto.equals(createdUser));
		verify(this.userRepository, times(1)).save(any());
	}
	
	@Test
	void givenNoUsers_WhenUpdateUserDebts_ThenSizeIsZero() {
		this.setUpMockRepositoryUsersWithDebts();
		this.userService.updateUserDebts(1L, 100D);
		verify(this.userRepository, times(1)).count();
		verify(this.userRepository, never()).save(any());
		
	}
	
	@Test
	void givenOneUser_WhenUpdateUserDebts_ThenDebtIsUnchanged() {
		this.setUpMockRepositoryUsersWithDebts(100D);
		this.userService.updateUserDebts(1L, 300D);
		assertEquals("100.00", this.userRepository.findById(1L).get().getBDDebt().toString());
	}
	
	@Test
	void givenTwoUsers_WhenUpdateUserDebts_ThenDebtIsDistributed() {
		this.setUpMockRepositoryUsersWithDebts(-100D, 200D);
		this.userService.updateUserDebts(2L, 80D);
		assertEquals("-60.00", this.userRepository.findById(1L).get().getBDDebt().toString());
		assertEquals("160.00", this.userRepository.findById(2L).get().getBDDebt().toString());
	}
	
	@Test
	void givenTwoUsers_WhenUpdateUserDebtsWithBigPrice_ThenDebtsProportionallyReverse() {
		this.setUpMockRepositoryUsersWithDebts(-45.60, 51.99);
		this.userService.updateUserDebts(2L, 149.77);
		assertEquals("29.29", this.userRepository.findById(1L).get().getBDDebt().toString());
		assertEquals("-22.90", this.userRepository.findById(2L).get().getBDDebt().toString());
	}
	
	@Test
	void givenThreeUsers_WhenUpdateUserDebtsWithIndivisiblePrice_ThenRemainderIsAssigned() {
		this.setUpMockRepositoryUsersWithDebts(0D, 0D, 0D);
		this.userService.updateUserDebts(2L, 100D);
		assertEquals("33.34", this.userRepository.findById(1L).get().getBDDebt().toString());
		assertEquals("-66.67", this.userRepository.findById(2L).get().getBDDebt().toString());
		assertEquals("33.33", this.userRepository.findById(3L).get().getBDDebt().toString());
	}
	
	@Test
	void givenFiveUsers_WhenUpdateUserDebtsWithIndivisibleDecimalPrice_ThenRemainderIsAssigned() {
		this.setUpMockRepositoryUsersWithDebts(0D, 0D, 0D, 0D, 0D);
		this.userService.updateUserDebts(5L, 100.03D);
		assertEquals("20.01", this.userRepository.findById(1L).get().getBDDebt().toString());
		assertEquals("20.01", this.userRepository.findById(2L).get().getBDDebt().toString());
		assertEquals("20.01", this.userRepository.findById(3L).get().getBDDebt().toString());
		assertEquals("20.00", this.userRepository.findById(4L).get().getBDDebt().toString());
		assertEquals("-80.03", this.userRepository.findById(5L).get().getBDDebt().toString());
	}

}
