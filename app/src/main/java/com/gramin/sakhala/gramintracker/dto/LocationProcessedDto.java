package com.gramin.sakhala.gramintracker.dto;

/**
 * Created by atulsakhala on 11/08/18.
 */

public class LocationProcessedDto {
    Double x,y,distance;

    public LocationProcessedDto(double x, double y, Double aDouble) {
        this.x = x;
        this.y = y;
        distance = aDouble;
    }

    @Override
    public String toString() {
        return "LocationProcessedDto{" +
                "x=" + x +
                ", y=" + y +
                ", distance=" + distance +
                '}';
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
