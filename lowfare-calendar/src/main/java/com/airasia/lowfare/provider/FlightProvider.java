package com.airasia.lowfare.provider;

import com.airasia.lowfare.model.FareRecord;

import java.util.List;

public interface FlightProvider {

    List<FareRecord> getCalendar();

}