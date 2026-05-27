package com.airasia.lowfare.service;

import com.airasia.lowfare.cache.CacheService;
import com.airasia.lowfare.dto.DayFare;
import com.airasia.lowfare.model.FareRecord;
import com.airasia.lowfare.provider.ProviderA;
import com.airasia.lowfare.provider.ProviderB;
import com.airasia.lowfare.provider.ProviderC;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Service
public class AggregationService {

        private final ProviderA providerA;
        private final ProviderB providerB;
        private final ProviderC providerC;
        private final Executor executor;
        private final CacheService cacheService;
        public AggregationService(ProviderA providerA, ProviderB providerB, ProviderC providerC, Executor executor, CacheService cacheService) {

                this.providerA = providerA;
                this.providerB = providerB;
                this.providerC = providerC;
                this.executor = executor;
                this.cacheService = cacheService;
                // this.cacheService = cacheService;
        };

        @CircuitBreaker(
                name="provider",
                fallbackMethod="fallback"
        )
        
        public List<DayFare> getLowestFares() {

                try {

                var a = CompletableFuture.supplyAsync(providerA::getCalendar,executor);

                var b = CompletableFuture.supplyAsync(providerB::getCalendar,executor);

                var c = CompletableFuture.supplyAsync(providerC::getCalendar,executor);

                CompletableFuture.allOf(a, b, c).join();

                List<FareRecord> all = new ArrayList<>();

                all.addAll(a.get());

                all.addAll(b.get());

                all.addAll(c.get());

                return all.stream().collect(

                                Collectors.toMap(

                                        FareRecord::getDate,

                                        FareRecord::getPrice,

                                        Math::min

                                )

                        ).entrySet().stream().map(

                                e -> new DayFare(

                                        e.getKey(),

                                        e.getValue()

                                )

                        )
                .sorted(Comparator.comparing(DayFare::getDate))
                .toList();

                }

                catch (Exception e) {

                        throw new RuntimeException(e);

                }

        }


        @SuppressWarnings("unchecked")
        public List<FareRecord>fallback( Exception ex ) {

        System.out.println(
                "Circuit breaker fallback triggered"
        );

        // Optional stale cache
        Object cached = cacheService.get("calendar");

        if (cached != null) {

            return(List<FareRecord>) cached;

        }

        return List.of();

    }


}