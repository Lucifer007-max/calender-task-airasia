package com.airasia.lowfare.pubsub;

import com.airasia.lowfare.dto.SoldOutEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PriceSoldOutPublisher {

    private final ApplicationEventPublisher publisher;

    public PriceSoldOutPublisher(ApplicationEventPublisher publisher){

        this.publisher = publisher;

    }

    public void publish(){

        publisher.publishEvent(new SoldOutEvent( "1", "DEL-BKK", 2L ));



    }

}