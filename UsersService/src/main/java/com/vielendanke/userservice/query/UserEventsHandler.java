package com.vielendanke.userservice.query;

import com.vielendanke.core.model.PaymentDetails;
import com.vielendanke.core.model.User;
import com.vielendanke.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card123")
                .cvv("123")
                .name("VIELEN DANKE")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        return User.builder()
                .firstName("Vielen")
                .lastName("Danke")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();
    }
    
    
}
