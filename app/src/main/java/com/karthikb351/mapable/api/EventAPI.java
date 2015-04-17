package com.karthikb351.mapable.api;

import com.karthikb351.mapable.models.Event;

import org.altbeacon.beacon.Beacon;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


public interface EventAPI {


    /*
    To get all events in a certain location.
     */
    @FormUrlEncoded
    @POST("/events/all")
    List<Event> events(@Field("latitude") String latitude, @Field("longitude") String longitude);

    /*
    To get beacons for a certain event.
     */
    @GET("/events/{event}/beacons")
    List<Beacon> beacons(@Path("event") String event_slug);

}
