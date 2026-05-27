package com.airasia.lowfare.pubsub;

import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Component;

import com.airasia.lowfare.dto.SoldOutEvent;

@Component
public class PriceSoldOutListener {

    private final PriceSoldOutConsumer consumer;

    public PriceSoldOutListener(PriceSoldOutConsumer consumer) {

        this.consumer =consumer;

    }

    @EventListener
    public void handle(SoldOutEvent event) {

        consumer.consume(event.getEventId(), event.getRoute(), event.getVersion());

        // System.out.println("ASYNC EVENT CONSUMED");

    }

}