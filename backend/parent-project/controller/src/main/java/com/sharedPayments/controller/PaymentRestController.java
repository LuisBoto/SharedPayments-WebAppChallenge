package com.sharedPayments.controller;

import java.util.List;

import javax.validation.Valid;

import com.sharedPayments.dto.PaymentDto;
import com.sharedPayments.service.PaymentService;

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
public class PaymentRestController {

    private PaymentService paymentService;
    
    public PaymentRestController(PaymentService paymentService) {
    	this.paymentService = paymentService;
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
    public HttpResponse<PaymentDto> createPayment(@Body @Valid PaymentDto newPayment) {
    	var result = HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.paymentService.createPayment(newPayment));
    	return result;
    }

        
}