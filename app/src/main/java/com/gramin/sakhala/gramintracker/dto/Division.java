package com.gramin.sakhala.gramintracker.dto;

public class Division implements DivisionData {
    String name;

    public Division(String d) {
        name = d;
    }

    public String getName() {
        return name;
    }
}
