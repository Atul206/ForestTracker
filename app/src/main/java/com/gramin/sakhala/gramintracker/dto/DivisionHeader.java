package com.gramin.sakhala.gramintracker.dto;

public class DivisionHeader  implements DivisionData{
    String divisionHeader;

    public DivisionHeader(String d) {
        divisionHeader = d;
    }

    public String getDivisionHeader() {
        return divisionHeader;
    }
}
