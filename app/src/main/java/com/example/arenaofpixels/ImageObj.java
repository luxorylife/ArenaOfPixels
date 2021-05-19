package com.example.arenaofpixels;

import java.io.Serializable;
import java.util.List;

public class ImageObj implements Serializable {

    public int id;
    public String uri;
    public List<String> sections;
    public int wins;
    public int loses;
    public int reports;
    public boolean isDeleted;

    public ImageObj(){

    }

    public ImageObj(int id, String uri, List<String> sections, int wins, int loses, int reports){
        this.id = id;
        this.uri = uri;
        this.sections = sections;
        this.wins = wins;
        this.loses = loses;
        this.reports = reports;
        this.isDeleted = false;
    }

}
