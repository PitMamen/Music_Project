package com.dlighttech.music.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhujiang on 16-6-22.
 */
public class MusicInfo implements Serializable, Parcelable {
    // 歌曲id
    private int musicId;
    // singer
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
    //专辑下歌曲的数目
    private int musicAlbumsNumber;


    // 文件路径
    private String musicPath;
    // 歌曲文件大小
    private long musicSize;


    public MusicInfo() {
    }

    public MusicInfo(int id, String singer, String musicName
            , long currTime, long totalTime, MusicState state
            , Bitmap musicAlbumsImage, String musicAlbumsName,int musicAlbumsNumber
            , String musicPath, long musicSize) {
        this.musicId = id;
        this.singer = singer;
        this.musicName = musicName;
        this.currTime = currTime;
        this.totalTime = totalTime;
        this.state = state;
        this.musicAlbumsImage = musicAlbumsImage;
        this.musicAlbumsName = musicAlbumsName;
        this.musicAlbumsNumber = musicAlbumsNumber;
        this.musicPath = musicPath;
        this.musicSize = musicSize;
    }

    protected MusicInfo(Parcel in) {
        musicId = in.readInt();
        singer = in.readString();
        musicName = in.readString();
        currTime = in.readLong();
        totalTime = in.readLong();
        musicAlbumsImage = in.readParcelable(Bitmap.class.getClassLoader());
        musicAlbumsName = in.readString();
        musicAlbumsNumber =in.readInt();
        musicPath = in.readString();
        musicSize = in.readLong();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

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

    public int getMusicAlbumsNumber() {
        return musicAlbumsNumber;
    }

    public void setMusicAlbumsNumber(int musicAlbumsNumber) {
        this.musicAlbumsNumber = musicAlbumsNumber;
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
                ", musicAlbumsName='" + musicAlbumsName + '\'' + "musicAlbumsNumber" + musicAlbumsNumber +
                ", musicPath='" + musicPath + '\'' +
                ", musicSize=" + musicSize +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(musicId);
        dest.writeString(singer);
        dest.writeString(musicName);
        dest.writeLong(currTime);
        dest.writeLong(totalTime);
        dest.writeParcelable(musicAlbumsImage, flags);
        dest.writeString(musicAlbumsName);
        dest.writeInt(musicAlbumsNumber);
        dest.writeString(musicPath);
        dest.writeLong(musicSize);
    }

    public enum MusicState {
        PAUSE, START, NEXT, NORMAL
    }
}
