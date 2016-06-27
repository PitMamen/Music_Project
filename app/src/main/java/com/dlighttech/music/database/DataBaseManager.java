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
 * <p/>
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
     * 判断是否有同名的歌单
     *
     * @param name
     * @return true 存在 ，false不存在
     */
    public boolean isExistsName(String name) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "select count(*) from " + SongListDataBase.SONG_LIST_TABLE
                        + " where " + SongListDataBase.SONG_LIST_NAME + "=?";

                c = database.rawQuery(sql, new String[]{name});
                if (c.moveToFirst()) {
                    int count = c.getInt(0);
                    if (count > 0) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
            return false;
        }

    }

    /**
     * 根据歌单名称获取歌单id
     *
     * @param name
     * @return
     */
    public int getSongListIdByName(String name) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "select " + SongListDataBase._ID + " from "
                        + SongListDataBase.SONG_LIST_TABLE
                        + " where " + SongListDataBase.SONG_LIST_NAME + "=?";

                c = database.rawQuery(sql, new String[]{name});
                if (c.moveToFirst()) {
                    int id = c.getInt(0);
                    if (id > 0) {
                        return id;
                    }
                }
                return -1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
            return -1;
        }
    }

    public int updateSongOfListBySongListId(String name) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "select " + SongListDataBase._ID + " from "
                        + SongListDataBase.SONG_LIST_TABLE
                        + " where " + SongListDataBase.SONG_LIST_NAME + "=?";

                c = database.rawQuery(sql, new String[]{name});
                if (c.moveToFirst()) {
                    int id = c.getInt(0);
                    if (id > 0) {
                        return id;
                    }
                }
                return -1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
            return -1;
        }
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
                Object[] bindArgs = new Object[]{songList.getName()
                        , songList.getCount()};
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
     * @param song
     * @return
     */
    public boolean insertSong(Song song) {
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
                        , song.getSongPath(), song.getSongListId()};
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
                    String count = c.getString(c.getColumnIndexOrThrow(SongListDataBase.SONG_LIST_COUNT));

                    SongList list = new SongList();
                    list.setId(id);
                    list.setName(name);
                    list.setCount(count);
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
