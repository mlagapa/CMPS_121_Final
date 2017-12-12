package com.example.court.finalproject;

/**
 * Created by mrlag on 12/6/2017.
 */


public class Songs
{

    private long mSongID;
    private String mSongTitle;

    public Songs(long id, String title){
        mSongID = id;
        mSongTitle = title;
    }

    public long getSongID(){
        return mSongID;
    }

    public String getSongTitle(){
        return mSongTitle;
    }
}
