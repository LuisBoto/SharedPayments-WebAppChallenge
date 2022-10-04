package com.sharedPayments;

import javax.persistence.Entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.runtime.Micronaut;

@Introspected(packages="com.sharedPayments.model", includedAnnotations=Entity.class)
public class Application {
	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}
}
