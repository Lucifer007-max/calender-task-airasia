package com.airasia.lowfare;

import com.airasia.lowfare.cache.CacheService;

import com.airasia.lowfare.pubsub.PriceSoldOutConsumer;

import com.airasia.lowfare.service.EventStateService;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PriceSoldOutConsumerTest {

    @Test
    void shouldInvalidateCacheOnEvent() {

        CacheService cache =

                mock(
                        CacheService.class
                );

        EventStateService state =

                new EventStateService();

        PriceSoldOutConsumer consumer =

                new PriceSoldOutConsumer(

                        cache,

                        state

                );

        consumer.consume(

                "event-1",

                "DEL-BKK",

                2L

        );

        verify(

                cache,

                times(1)

        )

        .evict(

                any()

        );

    }

}