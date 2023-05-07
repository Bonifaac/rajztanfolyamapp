package com.example.rajztanfolyam;

public class TanfolyamItem {
    private String name;
    private String eventDb;
    private String price;
    private String diff;

    public TanfolyamItem(String name, String eventDb, String price, String diff) {
        this.name = name;
        this.eventDb = eventDb;
        this.price = price;
        this.diff = diff;
    }

    public TanfolyamItem() {
    }

    public String getName() {
        return name;
    }

    public String getEventDb() {
        return eventDb;
    }

    public String getDiff() {
        return diff;
    }

    public String getPrice() {
        return price;
    }

}
