package com.airasia.lowfare;

import com.airasia.lowfare.cache.CacheService;
import com.airasia.lowfare.dto.CalendarResponse;
import com.airasia.lowfare.dto.DayFare;

import com.airasia.lowfare.service.AggregationService;
import com.airasia.lowfare.service.CalendarService;
import com.airasia.lowfare.service.CurrencyService;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

class CalendarServiceCacheTest {

    @Test
    void shouldFetchWhenCacheMiss() {

        AggregationService aggregation =
                mock(
                        AggregationService.class
                );

        CurrencyService currency =
                mock(
                        CurrencyService.class
                );

        CacheService cache =
                mock(
                        CacheService.class
                );


        when(

                cache.get(

                        anyString()

                )

        )

        .thenReturn(
                null
        );


        when(

                aggregation
                        .getLowestFares()

        )

        .thenReturn(

                List.of(

                        new DayFare(

                                "2026-05-01",

                                200

                        )

                )

        );


        when(

                currency.convert(

                        200,

                        "USD"

                )

        )

        .thenReturn(

                200

        );


        CalendarService service =

                new CalendarService(

                        aggregation,

                        currency,

                        cache

                );


        CalendarResponse response =

                service.getCalendar(

                        "DEL",

                        "BKK",

                        "2026-05",

                        "USD"

                );


        assertEquals(

                1,

                response
                        .getDays()
                        .size()

        );


        verify(

                aggregation,

                times(
                        1
                )

        )

        .getLowestFares();


        verify(

                cache

        )

        .put(

                anyString(),

                any()

        );

    }

}