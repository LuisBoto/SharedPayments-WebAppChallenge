package sharedPayments;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;

public class Application {
	public static void main(String[] args) {
		ApplicationContext.run(Environment.DEVELOPMENT);
		Micronaut.run(Application.class, args);
	}
}
