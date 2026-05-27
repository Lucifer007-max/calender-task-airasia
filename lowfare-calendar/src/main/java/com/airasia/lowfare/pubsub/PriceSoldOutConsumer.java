package com.airasia.lowfare.pubsub;

import com.airasia.lowfare.cache.CacheService;
import com.airasia.lowfare.service.EventStateService;

import org.springframework.stereotype.Service;

@Service
public class PriceSoldOutConsumer {

    private final CacheService cache;

    private final EventStateService eventStateService;

    public PriceSoldOutConsumer( CacheService cache, EventStateService eventStateService) {

        this.cache =  cache;
        this.eventStateService =  eventStateService;

    }

    public void consume( String eventId, String route, Long version) {

        if (eventStateService.processed(eventId)) {

            System.out.println("DUPLICATE EVENT");
            return;

        }

        if (eventStateService.stale(route, version)) {

            System.out.println("OLD EVENT");
            return;

        }

        cache.evict("calendar:" + route);
        System.out.println("EVENT APPLIED");

    }

}