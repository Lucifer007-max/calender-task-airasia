package com.airasia.lowfare;

import com.airasia.lowfare.dto.DayFare;

import com.airasia.lowfare.service.*;

import com.airasia.lowfare.dto.CalendarResponse;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

                        currency, null

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

    }

}