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
import sharedPayments.model.dto.MoneyMovementDto;
import sharedPayments.model.dto.PaymentDto;
import sharedPayments.service.PaymentService;
import sharedPayments.service.UserService;

@Validated
@Controller("/api/v1")
public class PaymentRestController {

    private UserService userService;
    private PaymentService paymentService;
    
    public PaymentRestController(UserService userService, PaymentService paymentService) {
    	this.userService = userService;
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
    @Transactional
    public HttpResponse<PaymentDto> createPayment(@Body @Valid PaymentDto newPayment) {
    	var result = HttpResponse
        		.status(HttpStatus.OK)
        		.body(
        				this.paymentService.createPayment(newPayment));
    	this.userService.updateUserDebts(newPayment.getPayerId(), newPayment.getPrice());
    	return result;
    }
    
    @Get("/movements")
    @Produces(MediaType.APPLICATION_JSON) 
    public HttpResponse<List<MoneyMovementDto>> getMoneyMovementsToCompensateDebt() {
    	return HttpResponse
    			.status(HttpStatus.OK)
    			.body(
    					this.userService.getMoneyMovementsToCompensateDebt());
    }
        
}