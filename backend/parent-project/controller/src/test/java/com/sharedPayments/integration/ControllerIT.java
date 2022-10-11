package com.sharedPayments.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import com.sharedPayments.dto.UserDto;
import com.sharedPayments.model.User;
import com.sharedPayments.service.UserService;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;

@MicronautTest
public class ControllerIT {
	
	@MockBean(UserService.class)
    public UserService dependency() {
		var userService = mock(UserService.class);
		var users = Arrays.asList(
				UserDto.builder().id(1L).name("Pepe").debt(new BigDecimal(18.0)).build(),
				UserDto.builder().id(2L).name("John").debt(new BigDecimal(53.99)).build(),  
				UserDto.builder().id(3L).name("Maria").debt(new BigDecimal(14.56)).build());
		when(userService.getUsers()).thenReturn(users);
		when(userService.createUser(any(UserDto.class))).thenAnswer(call -> {
				UserDto paramUser = call.getArgument(0);
				paramUser.setId(100L);
				User user = paramUser.toModel();
				return user.toDto();
			});
        return userService;
    }
	
	@Test
	public void givenUsers_WhenGetUsers_ThenBodyContainsAllUserData (RequestSpecification spec) {
		spec
			.given()
				.contentType("application/json")
			.when()
				.get("/api/v1/users")
			.then()
				.statusCode(200)
				.body(
						"id", hasItems(1, 2, 3),//, 4, 5),
						"name", hasItems("Pepe", "John", "Maria"),
						"debt", hasItems(18, 53.99F, 14.56F));
	}
	
	@Test
	public void givenNewUser_WhenPostUsers_ThenBodyContainsNewUserData (RequestSpecification spec) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("name", "ExampleTestUser");
		
		spec
		.given()
			.contentType("application/json")
			.body(requestBody.toString())
		.when()
			.post("/api/v1/users")
		.then()
			.statusCode(HttpStatus.CREATED.getCode())
			.body(
					"id", equalTo(100),
					"name", equalTo("ExampleTestUser"),
					"debt", equalTo(0f));
	}
	
	@Test
	public void givenNewInvalidUser_WhenPostUsers_ThenRequestError (RequestSpecification spec) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("debt", 20f);
		
		spec
		.given()
			.contentType("application/json")
			.body(requestBody.toString())
		.when()
			.post("/api/v1/users")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.getCode());
	}
	
}
