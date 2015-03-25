package com.karthikb351.mapable.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Event {
    public String name;
    public Building building;
    public String date_string;
    private Date date;

    public Event(String name, Building building,String date_string){
        this.name = name;
        this.building = building;
        this.date_string = date_string;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            this.date = dateFormat.parse(date_string);
        }
        catch (Exception e){
            this.date = new Date();
            e.printStackTrace();
        }
    }
}
