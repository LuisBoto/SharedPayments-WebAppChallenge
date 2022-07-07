package sharedPayments.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import sharedPayments.service.PaymentsService;

@Validated
@Controller("/api/v1")
public class PaymentsRestController {

    @Inject
    private PaymentsService paymentService;

    @Get("/payments")
    @Produces(MediaType.TEXT_PLAIN) 
    public String getAllPayments() {
        return this.paymentService.getPayments();
    }
        
}