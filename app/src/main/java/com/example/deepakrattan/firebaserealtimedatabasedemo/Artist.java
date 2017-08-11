package com.example.deepakrattan.firebaserealtimedatabasedemo;

/**
 * Created by deepak.rattan on 8/9/2017.
 */

public class Artist {
    private String id, name, genere;

    //Empty constructor is necessary while fetching data from FirebaseDatabase
    public Artist() {
    }

    public Artist(String id, String name, String genere) {
        this.id = id;
        this.name = name;
        this.genere = genere;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenere() {
        return genere;
    }
}
