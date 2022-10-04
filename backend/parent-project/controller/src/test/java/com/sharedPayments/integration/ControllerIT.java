package com.sharedPayments.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
				new UserDto(1L, "Pepe", 18.0),
				new UserDto(2L, "John", 53.99),
				new UserDto(3L, "Maria", 14.56));
		when(userService.getUsers()).thenReturn(users);
		when(userService.createUser(any(UserDto.class))).thenAnswer(call -> {
				UserDto paramUser = call.getArgument(0);
				User user = paramUser.toModel();
				user.setId(100L);
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
						"name", hasItems("Pepe", "John", "Maria"),//, "Carla", "Rebeca"),
						"debt", hasItems(18.0f, 53.99f, 14.56f));//, -286.76f, 200.21f));
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
