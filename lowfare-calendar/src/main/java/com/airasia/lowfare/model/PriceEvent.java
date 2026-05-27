package com.airasia.lowfare.model;

public class PriceEvent {

    private String eventId;

    private String route;

    private Long version;

    private String type;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(
            String eventId
    ) {
        this.eventId = eventId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(
            String route
    ) {
        this.route = route;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(
            Long version
    ) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(
            String type
    ) {
        this.type = type;
    }

}