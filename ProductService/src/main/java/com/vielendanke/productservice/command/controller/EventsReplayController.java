package com.vielendanke.productservice.command.controller;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/management")
@Slf4j
public class EventsReplayController {

    private final EventProcessingConfiguration configuration;

    @Autowired
    public EventsReplayController(EventProcessingConfiguration configuration) {
        this.configuration = configuration;
    }

    @PostMapping("/events/{eventName}/processors/{processorName}/reset")
    public ResponseEntity<Object> replayEvents(
            @PathVariable(name = "eventName") String eventName,
            @PathVariable(name = "processorName") String processorName
    ) {
        log.info(String.format("Reset on Event %s is processing", eventName));

        Optional<TrackingEventProcessor> trackingEventProcessor = configuration
                .eventProcessor(processorName, TrackingEventProcessor.class);

        if (trackingEventProcessor.isPresent()) {
            TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
            eventProcessor.shutDown();
            eventProcessor.resetTokens();
            eventProcessor.start();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(
                        "message",
                        String.format("Tracking event processor %s is not found", processorName))
                );
    }
}
