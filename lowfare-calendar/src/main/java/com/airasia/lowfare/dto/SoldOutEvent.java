package com.airasia.lowfare.dto;

public class SoldOutEvent {

    private String eventId;

    private String route;

    private Long version;

    public SoldOutEvent(){}

    public SoldOutEvent(String eventId,String route,Long version){

        this.eventId= eventId;

        this.route=route;

        this.version=version;

    }                                   

    public String getEventId(){
        return eventId;
    }

    public String getRoute(){
        return route;
    }

    public Long getVersion(){
        return version;
    }

}