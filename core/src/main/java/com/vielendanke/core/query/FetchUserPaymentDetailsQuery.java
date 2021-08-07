package com.vielendanke.core.query;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"userId"})
@ToString
public class FetchUserPaymentDetailsQuery {

    private final String userId;
}
