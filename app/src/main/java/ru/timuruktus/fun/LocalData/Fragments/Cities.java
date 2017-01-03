package ru.timuruktus.fun.LocalData.Fragments;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Contains an information about
 * recent version of cities DB
 * and all of the cities
 */
public class Cities implements Serializable {


    private int DBVersion;
    private String[] cities;


    public Cities(String[] cities, int DBVersion ){

        Arrays.sort(cities);
        this.cities = cities;
        this.DBVersion = DBVersion;

    }

    public int getDBVersion() {
        return DBVersion;
    }

    public String[] getCities() {
        return cities;
    }
}
