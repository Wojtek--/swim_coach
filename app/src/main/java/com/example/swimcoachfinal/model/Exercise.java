package com.example.swimcoachfinal.model;

public class Exercise {
    private int id;
    private int distance;
    private String style;

    public Exercise() {

    }

    public Exercise(int id, int distance, String style) {
        this.id = id;
        this.distance = distance;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
