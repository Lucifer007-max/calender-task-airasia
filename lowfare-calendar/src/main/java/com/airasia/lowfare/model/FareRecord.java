package com.airasia.lowfare.model;

public class FareRecord {

    private String date;

    private Integer price;

    public FareRecord() {
    }

    // FOR TESTING PURPOSES ONLY
    public FareRecord(

            String date,

            Integer price

    ) {

        this.date =
                date;

        this.price =
                price;

    }

    public String getDate() {
        return date;
    }

    public void setDate(
            String date
    ) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(
            Integer price
    ) {
        this.price = price;
    }

}