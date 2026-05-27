package com.airasia.lowfare;

// import com.airasia.lowfare.currency.CurrencyService;
import com.airasia.lowfare.service.CurrencyService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    private final CurrencyService service =
            new CurrencyService();

    @Test
    void shouldConvertUsd() {

        assertEquals(

                100,

                service.convert(
                        100,
                        "USD"
                )

        );

    }

    @Test
    void shouldConvertMyr() {

        assertEquals(

                397,

                service.convert(
                        100,
                        "MYR"
                )

        );

    }

    @Test
    void shouldConvertThb() {

        assertEquals(

                3271,

                service.convert(
                        100,
                        "THB"
                )

        );

    }

    @Test
    void unsupportedCurrency() {

        assertThrows(

                RuntimeException.class,

                () ->

                        service.convert(

                                100,

                                "XXX"

                        )

        );

    }

}