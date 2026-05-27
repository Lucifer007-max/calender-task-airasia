package com.airasia.lowfare.provider;

import com.airasia.lowfare.model.FareRecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class ProviderC implements FlightProvider {
    private final Random random = new Random();

    @Override
    public List<FareRecord> getCalendar() {

        try {

            // Uncomment the below lines to simulate random failures and test the circuit breaker and fallback mechanism

            
            // adding some random failures to test the circuit breaker and fallback mechanism
            // if(random.nextInt(10)<3){
            //     throw new RuntimeException( "Provider C failed"  );
            // }

            // Thread.sleep(2000);

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(

                    new ClassPathResource( "static/provider-c.json",
                            ProviderC.class.getClassLoader()
                    ).getInputStream(),

                    new TypeReference<List<FareRecord>>() {}

            );

        }

        catch (Exception e) {

            System.out.println(
                    "Provider C failed → fallback"
            );

            return List.of( );

        }

    }

}