package com.airasia.lowfare.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventStateService {

    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    private final ConcurrentHashMap<String,Long> versions =new ConcurrentHashMap<>();

    public boolean processed( String id ){

        return !processed.add(id);

    }

    public boolean stale( String route, Long version){

        Long current = versions.getOrDefault( route, 0L  );

        if( version  <=  current ){

            return true;

        }

        versions.put( route, version);

        return false;

    }

}