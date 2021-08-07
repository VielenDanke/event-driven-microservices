package com.vielendanke.productservice.command.interceptor;

import com.vielendanke.productservice.command.CreateProductCommand;
import com.vielendanke.productservice.core.repository.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    @Autowired
    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command) -> {

            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                String title = createProductCommand.getTitle();
                String id = createProductCommand.getProductId();

                boolean isExists = productLookupRepository.checkIfExists(
                        id, title);

                if (isExists) {
                    throw new IllegalStateException(String.format("Product with ID %s or title %s already exists", id, title));
                }
                log.info("Validation is passed");
            }

            return command;
        };
    }
}
