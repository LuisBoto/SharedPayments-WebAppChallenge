package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.Mockito.mock;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import sharedPayments.controller.UserRestController;
import sharedPayments.service.UserService;

@MicronautTest
public class ControllerIT extends GenericIT {
	
	//private UserService userService = mock(UserService.class);
	//private UserRestController userController = new UserRestController(this.userService);
	
	@Override
	protected long getInitialID() {
		return 100L;
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
						"id", hasItems(1, 2, 3, 4, 5),
						"name", hasItems("Pepe", "John", "Maria", "Carla", "Rebeca"),
						"debt", hasItems(18.0f, 53.99f, 14.56f, -286.76f, 200.21f));
	}
	
	@Test
	public void givenNewUser_WhenPostUsers_ThenBodyContainsNewUserData (RequestSpecification spec) {
		JSONObject requestBody = new JSONObject();
		requestBody.put("name", "ExampleTestUser");
		System.out.println(requestBody.toString());
		
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
	
}
