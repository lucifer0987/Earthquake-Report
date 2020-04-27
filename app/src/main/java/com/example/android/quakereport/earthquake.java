package com.example.android.quakereport;

import java.net.URI;

public class earthquake {

    private String  place, url;
    private Long time;
    private Double magnitude;

    public earthquake(Double magnitude, String place, Long time, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    public earthquake() {
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public Long getTime() {
        return time;
    }

    public String  getUrl() {
        return url;
    }
}
