package com.vielendanke.core.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"cardNumber"})
@ToString
public class PaymentDetails {

    private final String name;
    private final String cardNumber;
    private final int validUntilMonth;
    private final int validUntilYear;
    private final String cvv;
}
