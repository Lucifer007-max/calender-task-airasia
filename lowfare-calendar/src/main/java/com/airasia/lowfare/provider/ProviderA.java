package com.airasia.lowfare.provider;

import com.airasia.lowfare.model.FareRecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderA implements FlightProvider {

    @Override
    public List<FareRecord> getCalendar() {

        try {

            // Thread.sleep(10);

            ObjectMapper mapper =
                    new ObjectMapper();

            return mapper.readValue(
                    new ClassPathResource("static/provider-a.json", ProviderA.class.getClassLoader()).getInputStream(),
                    new TypeReference<List<FareRecord>>() {}
            );
        }

        catch (Exception e) {
            System.out.println(
                 e
            );
            System.out.println(
                    "Provider A failed → fallback"
            );

            return List.of( );

        }

    }

}