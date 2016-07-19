package com.dlighttech.music.model;

import java.util.Date;

/**
 * Created by zhujiang on 16-6-23.
 */
public class Song {

    private int id;
    private String name;
    private int songListId;
    private String singer;
    private String songPath;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getSongListId() {
        return songListId;
    }

    public void setSongListId(int songListId) {
        this.songListId = songListId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songListId=" + songListId +
                ", singer='" + singer + '\'' +
                ", songPath='" + songPath + '\'' +
                '}';
    }
}
