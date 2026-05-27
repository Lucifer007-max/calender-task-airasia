package com.airasia.lowfare.dto;

import java.io.Serializable;

import java.util.List;

public class CalendarResponse

implements Serializable {

private static final long serialVersionUID = 1L;

private String origin;

private String destination;

private String currency;

private List<DayFare> days;


public CalendarResponse(
String origin,
String destination,
String currency,
List<DayFare> days
){

this.origin=origin;

this.destination=destination;

this.currency=currency;

this.days=days;

}

public String getOrigin(){

return origin;

}

public String getDestination(){

return destination;

}

public String getCurrency(){

return currency;

}

public List<DayFare> getDays(){

return days;

}

}