package com.airasia.lowfare;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LowfareCalendarApplicationTests {

    @Autowired
    private ApplicationContext context;


    @Test
    void contextLoads() {

        assertNotNull(context);

    }


    @Test
    void applicationBeanExists() {

        assertTrue(

                context.containsBean(

                        "lowfareCalendarApplication"

                )

        );

    }


    @Test
    void shouldStartApplication() {

        assertNotNull(

                context.getBean(

                        LowfareCalendarApplication.class

                )

        );

    }

}