package com.sharedPayments.controller;

import java.util.List;

import javax.validation.Valid;

import com.sharedPayments.dto.UserDto;
import com.sharedPayments.service.UserService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;

@Validated
@Controller("/api/v1")
public class UserRestController {

    private UserService userService;
    
    public UserRestController(UserService userService) {
    	this.userService = userService;
    }

    @Get("/users")
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<List<UserDto>> getAllUsers() {
        return HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.userService.getUsers());
    }
    
    @Post("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<UserDto> createUser(@Body @Valid UserDto newUser) {
    	return HttpResponse
    			.status(HttpStatus.CREATED)
    			.body(
    					this.userService.createUser(newUser));
    }
        
}