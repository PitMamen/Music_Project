package com.dlighttech.music.model;

import android.graphics.Bitmap;

/**
 * Created by zhujiang on 16-6-22.
 */
public class MusicInfo {
    // 歌曲id
    private int musicId;
    // 歌手
    private String singer;
    // 歌曲名
    private String musicName;
    // 歌曲播放当前时间
    private long currTime;
    // 歌曲播放总时间
    private long totalTime;
    // 歌曲当前状态
    private MusicState state = MusicState.NORMAL;
    // 歌曲专辑图片
    private Bitmap musicAlbumsImage;
    // 歌曲专辑名
    private String musicAlbumsName;
    // 文件路径
    private String musicPath;
    // 歌曲文件大小
    private long musicSize;


    public MusicInfo() {
    }

    public MusicInfo(int id,String singer, String musicName
            , long currTime, long totalTime, MusicState state
            , Bitmap musicAlbumsImage, String musicAlbumsName
            , String musicPath, long musicSize) {
        this.musicId = id;
        this.singer = singer;
        this.musicName = musicName;
        this.currTime = currTime;
        this.totalTime = totalTime;
        this.state = state;
        this.musicAlbumsImage = musicAlbumsImage;
        this.musicAlbumsName = musicAlbumsName;
        this.musicPath = musicPath;
        this.musicSize = musicSize;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public long getCurrTime() {
        return currTime;
    }

    public void setCurrTime(long currTime) {
        this.currTime = currTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public MusicState getState() {
        return state;
    }

    public void setState(MusicState state) {
        this.state = state;
    }

    public Bitmap getMusicAlbumsImage() {
        return musicAlbumsImage;
    }

    public void setMusicAlbumsImage(Bitmap musicAlbumsImage) {
        this.musicAlbumsImage = musicAlbumsImage;
    }

    public String getMusicAlbumsName() {
        return musicAlbumsName;
    }

    public void setMusicAlbumsName(String musicAlbumsName) {
        this.musicAlbumsName = musicAlbumsName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public long getMusicSize() {
        return musicSize;
    }

    public void setMusicSize(long musicSize) {
        this.musicSize = musicSize;
    }


    @Override
    public String toString() {
        return "MusicInfo{" +
                "musicId=" + musicId +
                ", singer='" + singer + '\'' +
                ", musicName='" + musicName + '\'' +
                ", currTime=" + currTime +
                ", totalTime=" + totalTime +
                ", state=" + state +
                ", musicAlbumsImage=" + musicAlbumsImage +
                ", musicAlbumsName='" + musicAlbumsName + '\'' +
                ", musicPath='" + musicPath + '\'' +
                ", musicSize=" + musicSize +
                '}';
    }

    public enum MusicState {
        PAUSE, START, NEXT, NORMAL
    }
}
