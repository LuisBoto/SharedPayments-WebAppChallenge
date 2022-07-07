package sharedPayments.controller;

import java.util.List;

import javax.validation.Valid;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import sharedPayments.model.User;
import sharedPayments.service.UserService;

@Validated
@Controller("/api/v1")
public class PaymentsRestController {

    @Inject
    private UserService userService;

    @Get("/users")
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<List<User>> getAllUsers() {
        return HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.userService.getUsers());
    }
    
    @Post("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<User> createUser(@Body @Valid User newUser) {
    	return HttpResponse
    			.status(HttpStatus.CREATED)
    			.body(
    					this.userService.createUser(newUser));
    }
        
}