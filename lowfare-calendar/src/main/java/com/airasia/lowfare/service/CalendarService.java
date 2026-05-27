package com.airasia.lowfare.service;

import com.airasia.lowfare.cache.CacheService;
import com.airasia.lowfare.dto.CalendarResponse;
import com.airasia.lowfare.dto.DayFare;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final AggregationService aggregationService;

    private final CurrencyService currencyService;

    private final CacheService cacheService;

    private final ConcurrentHashMap<String, ReentrantLock>locks = new ConcurrentHashMap<>();


    public CalendarService(AggregationService aggregationService,CurrencyService currencyService, CacheService cacheService) {

        this.aggregationService = aggregationService;

        this.currencyService = currencyService;

        this.cacheService = cacheService;

    }


    public CalendarResponse getCalendar( String origin, String destination, String month, String currency ) {

        String key =  "calendar:" + origin + ":" + destination + ":" + month  + ":" + currency;


        Object cached =cacheService.get(key);

        if (cached != null) {

            return (CalendarResponse) cached;

        }


        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());


        lock.lock();

        try {

            cached = cacheService.get(key);

            if (cached != null) {

                return (CalendarResponse) cached;

            }


            List<DayFare> fares =  aggregationService.getLowestFares().stream().map(fare -> new DayFare( 

                                    fare.getDate(),

                                    currencyService.convert(fare.getLowestFare(), currency))).collect( Collectors.toList());


            CalendarResponse response = new CalendarResponse( origin, destination, currency, fares);
            cacheService.put( key, response );

            return response;

        }

        finally {

            lock.unlock();

        }

    }

}