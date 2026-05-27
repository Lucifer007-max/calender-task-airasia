package com.airasia.lowfare.controller;

import com.airasia.lowfare.dto.CalendarResponse;
import com.airasia.lowfare.pubsub.PriceSoldOutConsumer;
import com.airasia.lowfare.pubsub.PriceSoldOutPublisher;
import com.airasia.lowfare.service.CalendarService;

import jakarta.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
@Validated
public class CalendarController {

    private final CalendarService calendarService;
    private final PriceSoldOutConsumer consumer;
    private final PriceSoldOutPublisher publisher;

    public CalendarController( CalendarService calendarService, PriceSoldOutConsumer consumer, PriceSoldOutPublisher publisher ) {
        this.calendarService = calendarService;
        this.consumer = consumer;
        this.publisher = publisher;
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(
        @RequestParam @NotBlank(message = "origin is required") String origin,

        @RequestParam @NotBlank(message = "destination is required") String destination,

        @RequestParam @NotBlank(message = "month is required") String month,

        @RequestParam @NotBlank(message = "currency is required") String currency
     ) {
        
        return calendarService.getCalendar(
                origin,
                destination,
                month,
                currency
        );
    }

    @GetMapping("/simulate")
    public String simulate() {

        consumer.consume( "1", "DEL-BKK", 2L);

        consumer.consume( "1", "DEL-BKK", 2L);

        consumer.consume( "2",  "DEL-BKK", 1L);

        return "simulation complete";

    }


    @GetMapping("/publish")
    public String publish(PriceSoldOutPublisher p){

        publisher.publish();

        return "published";

    }
}