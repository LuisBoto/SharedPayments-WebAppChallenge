package sharedPayments.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/api/v1/") 
public class PaymentsRestController {

    @Get("payments")
    @Produces(MediaType.TEXT_PLAIN) 
    public String getAllPayments() {
        return "Hello World"; 
    }
    
}
