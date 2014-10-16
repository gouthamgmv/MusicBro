package com.AAAG.MusicBro;

public class Song {
    public String name;
    public String path;
    public String album;
    public String genre;
    public String artist;
    public String emotion;
    public String mood;
    public String lyric;
    public int thundercount=0, drizzlecount=0, raincount=0, snowcount=0, atmospherecount=0, sunnycount=0, extremecount=0 ;
    public int agegroup;
    public int count;
    public int rating;
    public boolean retrieved;
    public boolean isadded1, isadded2, isadded3, isadded4, isadded5, isadded6;

    public void incrementCount(){
        this.count++;
    }

    public void rate(int value){
        this.rating = value;
    }

}
