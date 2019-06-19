package com.s9986.becomeaninfluencer.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START comment_class]
@IgnoreExtraProperties
public class Shop {
    public String longitude;
    public String latitude;
    public String uid;
    public String name;
    public String type;
    public String radius;
    public String image;

    public Shop() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Shop(String uid, String name, String type, String radius, String longitude, String latitude) {
        this.uid = uid;
        this.name = name;
        this.type = type;
        this.radius = radius;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public boolean hasLongitude(){
        return (longitude != null && longitude.length() > 0);
    }
    public boolean hasLatitude(){
        return (latitude != null && latitude.length() > 0);
    }

    public double getLongitude(){
        return Double.parseDouble(longitude);
    }
    public double getLatitude(){
        return Double.parseDouble(latitude);
    }
    public double getRadius(){
        return Double.parseDouble(radius);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("type", type);
        result.put("radius", radius);
        result.put("longitude", longitude);
        result.put("latitude", latitude);

        return result;
    }

}
// [END comment_class]
