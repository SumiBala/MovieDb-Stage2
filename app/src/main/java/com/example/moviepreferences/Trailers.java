package com.example.moviepreferences;

public class Trailers {
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Trailers(String name, String key) {
        this.key = key;
        this.name = name;
    }

}
