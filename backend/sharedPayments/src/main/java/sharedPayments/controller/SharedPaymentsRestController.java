package sharedPayments.controller;

import java.util.List;

import javax.transaction.Transactional;
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
import sharedPayments.model.Payment;
import sharedPayments.model.User;
import sharedPayments.model.dto.PaymentDto;
import sharedPayments.service.PaymentService;
import sharedPayments.service.UserService;

@Validated
@Controller("/api/v1")
public class SharedPaymentsRestController {

    @Inject
    private UserService userService;
    
    @Inject
    private PaymentService paymentService;

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
    
    @Get("/payments")
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<List<PaymentDto>> getAllPayments() {
        return HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.paymentService.getPayments());
    }
    
    @Post("/payments")
    @Produces(MediaType.APPLICATION_JSON) 
    @Transactional
    public HttpResponse<PaymentDto> createPayment(@Body PaymentDto newPayment) {
    	var result = HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.paymentService.createPayment(newPayment, 
        						userService.getUser(newPayment.getPayerId())));
    	this.userService.updateUserDebts(newPayment.getPayerId(), newPayment.getPrice());
    	return result;
    }
        
}