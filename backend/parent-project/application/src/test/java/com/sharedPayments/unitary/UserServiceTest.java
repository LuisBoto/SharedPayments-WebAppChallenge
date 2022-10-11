package com.sharedPayments.unitary;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.sharedPayments.dto.UserDto;
import com.sharedPayments.model.User;
import com.sharedPayments.ports.UserRepository;
import com.sharedPayments.service.UserService;

public class UserServiceTest {
	
	private UserRepository userRepository = mock(UserRepository.class);
	private UserService userService = new UserService(this.userRepository);
	
	private void setUpMockRepositoryUsersWithDebts(Double... debts) {
		List<User> users = new ArrayList<User>();
		for (int i=0; i<debts.length; i++) {
			User user = new User(Integer.toUnsignedLong(i+1), "Name", new BigDecimal(debts[i]));
			users.add(user);
		}
		
		when(this.userRepository.findAll()).thenReturn(users);
		when(this.userRepository.findById(any(Long.class))).thenAnswer(call -> {
			return users.get(((Long) call.getArgument(0)).intValue()-1);
		});
		when(this.userRepository.count()).thenReturn(users.size());
		when(this.userRepository.save(any(User.class))).thenAnswer(
				call -> {
					User paramUser = call.getArgument(0);
					users.remove(paramUser);
/*
					for (User u : users) 
						if (u.getId() == paramUser.getId()) {
							u.setDebt(paramUser.getDebt());
							return u;
						}*/
					users.add(paramUser);
					return paramUser;
				});
	}
	
	private String getDebtForMockUser(Long Id) {
		return this.userRepository.findById(Id).getDebt().toString();
	}
	
	@Test
	void givenTwoUsers_WhenGetAllUsers_ThenAllUsersAreReturned() {
		List<User> users = Arrays.asList(
				User.builder().name("Juana").build(),
				User.builder().name("Mark").build()
				);
		when(this.userRepository.findAll()).thenReturn(users);

		assertThat(this.userService.getUsers(),
				is(users.stream().map(user -> user.toDto()).collect(Collectors.toList())));
	}
	
	@Test
	void givenNewValidUser_WhenCreateUser_ThenUserIsSaved() {
		UserDto userDto = UserDto.builder().id(2L).name("Marcos").debt(new BigDecimal(50.0)).build();  
		
		when(this.userRepository.save(any(User.class))).then(call -> {
			User result = userDto.toModel();
			return result;
		});
		
		UserDto createdUser = this.userService.createUser(userDto);
		assertEquals(userDto, createdUser);
		verify(this.userRepository).save(any());
	}
	
	@Test
	void givenNoUsers_WhenUpdateUserDebts_ThenSizeIsZero() {
		this.setUpMockRepositoryUsersWithDebts();
		this.userService.updateUserDebts(1L,  new BigDecimal(100D));
		verify(this.userRepository, times(1)).count();
		verify(this.userRepository, never()).save(any());
		
	}
	
	@Test
	void givenOneUser_WhenUpdateUserDebts_ThenDebtIsUnchanged() {
		this.setUpMockRepositoryUsersWithDebts(100D);
		this.userService.updateUserDebts(1L, new BigDecimal(300D));
		assertEquals("100.00", this.getDebtForMockUser(1L));
	}
	
	@Test
	void givenTwoUsers_WhenUpdateUserDebts_ThenDebtIsDistributed() {
		this.setUpMockRepositoryUsersWithDebts(-100D, 200D);
		this.userService.updateUserDebts(2L,  new BigDecimal(80D));
		assertEquals("-60.00", this.getDebtForMockUser(1L));
		assertEquals("160.00", this.getDebtForMockUser(2L));
	}
	
	@Test
	void givenTwoUsers_WhenUpdateUserDebtsWithBigPrice_ThenDebtsProportionallyReverse() {
		this.setUpMockRepositoryUsersWithDebts(-45.60, 51.99);
		this.userService.updateUserDebts(2L,  new BigDecimal(149.77));
		assertEquals("29.29", this.getDebtForMockUser(1L));
		assertEquals("-22.90", this.getDebtForMockUser(2L));
	}
	
	@Test
	void givenThreeUsers_WhenUpdateUserDebtsWithIndivisiblePrice_ThenRemainderIsAssigned() {
		this.setUpMockRepositoryUsersWithDebts(0D, 0D, 0D);
		this.userService.updateUserDebts(2L,  new BigDecimal(100D));
		assertEquals("33.34", this.getDebtForMockUser(1L));
		assertEquals("-66.67", this.getDebtForMockUser(2L));
		assertEquals("33.33", this.getDebtForMockUser(3L));
	}
	
	@Test
	void givenFiveUsers_WhenUpdateUserDebtsWithIndivisibleDecimalPrice_ThenRemainderIsAssigned() {
		this.setUpMockRepositoryUsersWithDebts(0D, 0D, 0D, 0D, 0D);
		this.userService.updateUserDebts(5L,  new BigDecimal(100.03D));
		assertEquals("20.01", this.getDebtForMockUser(1L));
		assertEquals("20.01", this.getDebtForMockUser(2L));
		assertEquals("20.01", this.getDebtForMockUser(3L));
		assertEquals("20.00", this.getDebtForMockUser(4L));
		assertEquals("-80.03", this.getDebtForMockUser(5L));
	}

}
