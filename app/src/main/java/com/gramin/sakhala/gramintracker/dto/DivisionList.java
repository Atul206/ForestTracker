package com.gramin.sakhala.gramintracker.dto;

import java.util.ArrayList;
import java.util.List;

public class DivisionList {
    List<String> divisionList = new ArrayList<>();
    List<String> subDivision = new ArrayList<>();
    List<DivisionData> divisionData;
    String lastDivision;

    public DivisionList() {
        divisionData = new ArrayList<>();
        setDivisionList();
        int i = 0;
        for (String d : divisionList) {
            if (!d.equals(lastDivision)) {
                divisionData.add(new DivisionHeader(d));
            } else {
                divisionData.add(new Division(subDivision.get(i)));
            }
            lastDivision = d;
            i++;
        }
    }

    private void setDivisionList() {
        subDivision.add("Bilaspur");
        subDivision.add("Dharamjaigarh");
        subDivision.add("Janjgir");
        subDivision.add("Katghora");
        subDivision.add("Korba");
        subDivision.add("Marwahi");
        subDivision.add("Mungeli");
        subDivision.add("Raigerh");
        subDivision.add("Balod");
        subDivision.add("Durg");
        subDivision.add("Kawardha");
        subDivision.add("Khairagarh");
        subDivision.add("Rajnandgaon");
        subDivision.add("East Bhanupratappur");
        subDivision.add("Kanker");
        subDivision.add("Narayanpur");
        subDivision.add("North Kondagaon");
        subDivision.add("South Kondagaon");
        subDivision.add("West Bhanupratappur");
        subDivision.add("Balodabazar");
        subDivision.add("Dhamtari");
        subDivision.add("Gariaband");
        subDivision.add("Mahasamund");
        subDivision.add("Raipur");
        subDivision.add("Bastar");
        subDivision.add("Bijapur");
        subDivision.add("Dantewara");
        subDivision.add("Sukma");
        subDivision.add("Balrampur");
        subDivision.add("Jashpur");
        subDivision.add("Koria");
        subDivision.add("Manedragarh");
        subDivision.add("Surajpur");
        subDivision.add("Surguja");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Bilaspur");
        divisionList.add("Durg");
        divisionList.add("Durg");
        divisionList.add("Durg");
        divisionList.add("Durg");
        divisionList.add("Durg");
        divisionList.add("Kanker");
        divisionList.add("Kanker");
        divisionList.add("Kanker");
        divisionList.add("Kanker");
        divisionList.add("Kanker");
        divisionList.add("Kanker");
        divisionList.add("Raipur");
        divisionList.add("Raipur");
        divisionList.add("Raipur");
        divisionList.add("Raipur");
        divisionList.add("Raipur");
        divisionList.add("Jagdalpur");
        divisionList.add("Jagdalpur");
        divisionList.add("Jagdalpur");
        divisionList.add("Jagdalpur");
        divisionList.add("Surguja");
        divisionList.add("Surguja");
        divisionList.add("Surguja");
        divisionList.add("Surguja");
        divisionList.add("Surguja");
        divisionList.add("Surguja");
    }

    public List<DivisionData> getDivisionData() {
        return divisionData;
    }
}
