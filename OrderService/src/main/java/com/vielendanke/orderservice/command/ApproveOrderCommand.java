package com.vielendanke.orderservice.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
@AllArgsConstructor
public class ApproveOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
}
