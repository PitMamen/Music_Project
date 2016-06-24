package com.dlighttech.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhujiang on 16-6-23.
 */
public class SongListDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SongList";

    public static final String _ID = "_id";

    /**
     * 歌单表
     */
    public static final String SONG_LIST_NAME = "name";
    public static final String SONG_LIST_COUNT = "count";
    public static final String SONG_LIST_TABLE = "song_list";

    /**
     * 歌单表下的歌曲表
     */
    public static final String SONG_TABLE = "song";
    public static final String SONG_NAME = "name";
    public static final String SINGER = "singer";
    public static final String SONG_PATH = "song_path";
    public static final String SONG_LIST_ID = "song_list_id";


    public SongListDataBase(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sb = new StringBuilder();
        sb.append("create table " + SONG_LIST_TABLE + "(");
        sb.append(_ID + " integer primary key autoincrement");
        sb.append("," + SONG_LIST_NAME + " text");
        sb.append("," + SONG_LIST_COUNT + " text");
        sb.append(")");
        db.execSQL(sb.toString());


        sb = new StringBuilder();
        sb.append("create table " + SONG_TABLE + "(");
        sb.append(_ID + " integer primary key autoincrement");
        sb.append("," + SONG_NAME + " text");
        sb.append("," + SINGER + " text");
        sb.append("," + SONG_PATH + " text");
        sb.append("," + SONG_LIST_ID + " integer");
        sb.append(")");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
