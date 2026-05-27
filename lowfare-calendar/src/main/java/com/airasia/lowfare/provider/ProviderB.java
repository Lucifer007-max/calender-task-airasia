package com.airasia.lowfare.provider;

import com.airasia.lowfare.model.FareRecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderB implements FlightProvider {

    @Override
    public List<FareRecord> getCalendar() {

        try {

            // Thread.sleep(2000);

            ObjectMapper mapper =
                    new ObjectMapper();

            return mapper.readValue(

                    new ClassPathResource(
                            "static/provider-b.json",
                            ProviderB.class.getClassLoader()
                    ).getInputStream(),

                    new TypeReference<List<FareRecord>>() {}

            );

        }

        catch (Exception e) {

            System.out.println(
                    "Provider B failed → fallback"
            );

            return List.of( );

        }

    }

}