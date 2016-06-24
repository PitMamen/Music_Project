package com.dlighttech.music.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dlighttech.music.model.Song;
import com.dlighttech.music.model.SongList;

import java.util.ArrayList;

/**
 * 用于处理歌单与歌单下歌曲信息的数据存储
 *
 * Created by zhujiang on 16-6-23.
 */
public class DataBaseManager {

    private static DataBaseManager dbManager;
    private SongListDataBase mDataBase;

    private DataBaseManager(Context context) {
        mDataBase = new SongListDataBase(context, 1);
    }

    public static DataBaseManager getInstance(Context context) {
        if (dbManager == null) {
            synchronized (DataBaseManager.class) {
                if (dbManager == null) {
                    dbManager = new DataBaseManager(context);
                }
            }
        }
        return dbManager;
    }

    /**
     * 插入一个歌单
     *
     * @param songList
     * @return
     */
    public boolean insertSongList(SongList songList) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "insert into " + SongListDataBase.SONG_LIST_TABLE
                        + "(" + SongListDataBase.SONG_LIST_NAME
                        + "," + SongListDataBase.SONG_LIST_COUNT + ")"
                        + " values (?,?)";
                Object[] bindArgs = new Object[]{songList.getName(), songList.getCount()};
                database.execSQL(sql, bindArgs);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (database != null)
                    database.close();
            }
        }
    }

    /**
     * 插入一首歌到一个歌单中
     *
     * @param songListId
     * @param song
     * @return
     */
    public boolean insertSong(int songListId, Song song) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;

            try {
                database = mDataBase.getReadableDatabase();
                String sql = "insert into " + SongListDataBase.SONG_TABLE
                        + "(" + SongListDataBase.SONG_NAME
                        + "," + SongListDataBase.SINGER
                        + "," + SongListDataBase.SONG_PATH
                        + "," + SongListDataBase.SONG_LIST_ID
                        + ") values (?,?,?,?)";
                Object[] bindArgs = new Object[]{song.getName(), song.getSinger()
                        , song.getSongPath(), songListId};
                database.execSQL(sql, bindArgs);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (database != null) {
                    database.close();
                }
            }
        }
    }

    /**
     * 获取所有歌单
     *
     * @return
     */
    public ArrayList<SongList> getAllSongList() {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                ArrayList<SongList> songLists = new ArrayList<SongList>();

                database = mDataBase.getReadableDatabase();

                String sql = "select * from " + SongListDataBase.SONG_LIST_TABLE;

                c = database.rawQuery(sql, null);
                while (c.moveToNext()) {
                    int id = c.getInt(c
                            .getColumnIndexOrThrow(SongListDataBase._ID));
                    String name = c.getString(c
                            .getColumnIndexOrThrow(SongListDataBase.SONG_LIST_NAME));
                    int count = c.getInt(c.getColumnIndexOrThrow(SongListDataBase.SONG_LIST_COUNT));

                    SongList list = new SongList();
                    list.setId(id);
                    list.setName(name);
                    list.setCount(String.valueOf(count));
                    songLists.add(list);
                }
                return songLists;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
        }
    }

    /**
     * 根据歌单id获取歌曲列表
     *
     * @param id
     * @return
     */
    public ArrayList<Song> getSongByListId(int id) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                ArrayList<Song> songs = new ArrayList<Song>();

                database = mDataBase.getReadableDatabase();
                String sql = "select * from " + SongListDataBase.SONG_TABLE
                        + " where " + SongListDataBase.SONG_LIST_ID + "=?";
                c = database.rawQuery(sql, new String[]{String.valueOf(id)});
                while (c.moveToNext()) {
                    int songId = c.getInt(c.getColumnIndexOrThrow(SongListDataBase._ID));
                    String songName = c.getString(c.getColumnIndexOrThrow(SongListDataBase.SONG_NAME));
                    String singer = c.getString(c.getColumnIndexOrThrow(SongListDataBase.SINGER));
                    String songPath = c.getString(c.getColumnIndexOrThrow(SongListDataBase.SONG_PATH));
                    int songListId = c.getInt(c.getColumnIndexOrThrow(SongListDataBase.SONG_LIST_ID));

                    Song song = new Song();
                    song.setId(songId);
                    song.setName(songName);
                    song.setSinger(singer);
                    song.setSongPath(songPath);
                    song.setSongListId(songListId);
                    songs.add(song);
                }
                return songs;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
            return null;
        }
    }

}
