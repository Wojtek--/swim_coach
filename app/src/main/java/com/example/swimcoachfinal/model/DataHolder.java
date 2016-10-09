package com.example.swimcoachfinal.model;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class DataHolder {
    private int selectedStyle;
    private int selectedDistance;
    private List<String> style;
    private List<Integer> dystanse;
    private ArrayAdapter<String> style_adapter;
    private ArrayAdapter<Integer> distance_adapter;
    private Boolean checked;

    public DataHolder(Context parent) {
        prepareListData();
        style_adapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item, style);
        distance_adapter = new ArrayAdapter<Integer>(parent, android.R.layout.simple_spinner_item, dystanse);
        style_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public ArrayAdapter<String> getStyleAdapter() {
        return style_adapter;
    }

    public ArrayAdapter<Integer> getDistanceAdapter() {
        return distance_adapter;
    }

    public String getStyle() {
        return style_adapter.getItem(selectedStyle);
    }

    public int getSelectedStyle() {
        return selectedStyle;
    }

    public void setSelectedStyle(int selected) {
        this.selectedStyle = selected;
    }

    public Integer getDistance() {
        return distance_adapter.getItem(selectedDistance);
    }

    public int getSelectedDistance() {
        return selectedDistance;
    }

    public void setSelectedDistance(int selected) {
        this.selectedDistance = selected;
    }

    private void prepareListData() {
        style = new ArrayList<String>();
        style.add("Dowolny");
        style.add("Grzbietowy");
        style.add("Klasyczny");
        style.add("Motylkowy");

        List<Integer> tmp = new ArrayList<Integer>();
        for (int i = 1; i <= 60; i++) {
            tmp.add(25 * i);
        }
        dystanse = new ArrayList<Integer>(tmp);
    }
}
