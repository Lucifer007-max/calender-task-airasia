package com.airasia.lowfare.dto;

import java.io.Serializable;

public class DayFare implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;

    private Integer lowestFare;


    public DayFare() {
    }


    public DayFare(
            String date,
            Integer lowestFare
    ) {

        this.date = date;

        this.lowestFare = lowestFare;

    }


    public String getDate() {

        return date;

    }


    public void setDate(
            String date
    ) {

        this.date = date;

    }


    public Integer getLowestFare() {

        return lowestFare;

    }


    public void setLowestFare(
            Integer lowestFare
    ) {

        this.lowestFare = lowestFare;

    }

}