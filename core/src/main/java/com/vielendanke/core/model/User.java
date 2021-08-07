package com.vielendanke.core.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"userId"})
@ToString
public class User {

    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;
}
