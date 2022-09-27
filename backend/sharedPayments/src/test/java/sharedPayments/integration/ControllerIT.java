package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;

@MicronautTest
public class ControllerIT {
	
	@Test
	public void testHelloEndpoint(RequestSpecification spec) {
		spec.
			when()
				.get("/hello")
			.then()
				.statusCode(404);
	}
	
}
