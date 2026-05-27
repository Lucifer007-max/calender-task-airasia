package com.airasia.lowfare;

import com.airasia.lowfare.provider.ProviderA;
import com.airasia.lowfare.provider.ProviderB;
import com.airasia.lowfare.provider.ProviderC;

import com.airasia.lowfare.model.FareRecord;

import com.airasia.lowfare.dto.DayFare;

import com.airasia.lowfare.service.AggregationService;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class AggregationServiceTest {

    @Test
    void shouldReturnLowestFareAcrossProviders()
            throws Exception {

        ProviderA a =
                mock(
                        ProviderA.class
                );

        ProviderB b =
                mock(
                        ProviderB.class
                );

        ProviderC c =
                mock(
                        ProviderC.class
                );

        Executor executor =
                Runnable::run;

        when(
                a.getCalendar()
        )

        .thenReturn(

                List.of(

                        new FareRecord(
                                "2026-05-01",
                                430
                        )

                )

        );

        when(
                b.getCalendar()
        )

        .thenReturn(

                List.of(

                        new FareRecord(
                                "2026-05-01",
                                390
                        )

                )

        );

        when(
                c.getCalendar()
        )

        .thenReturn(

                List.of(

                        new FareRecord(
                                "2026-05-01",
                                200
                        )

                )

        );

        AggregationService service =

                new AggregationService(

                        a,

                        b,

                        c,

                        executor, null

                );

        List<DayFare> result =

                service
                        .getLowestFares();

        assertEquals(

                200,

                result
                        .get(0)
                        .getLowestFare()

        );

    }

}