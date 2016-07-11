package com.dlighttech.music.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlighttech.music.model.MusicInfo;
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
     * @param name 歌单名称
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

    /**
     * 根据歌单id更新歌单曲目数量
     *
     * @param id 歌单id
     * @return
     */
    public boolean updateSongOfListBySongListId(int id, int count) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "update  " + SongListDataBase.SONG_LIST_TABLE
                        + " set " + SongListDataBase.SONG_LIST_COUNT + "=?"
                        + " where " + SongListDataBase._ID + "=?";
                database.execSQL(sql, new Object[]{count, id});
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (database != null) database.close();
            }
            return false;
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


    public SongList getSongListById(int id) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            Cursor c = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "select * from " + SongListDataBase.SONG_LIST_TABLE
                        + " where " + SongListDataBase._ID + "=?";
                c = database.rawQuery(sql, new String[]{String.valueOf(id)});
                if (c.moveToFirst()) {
                    int songlistId = c.getInt(c.getColumnIndexOrThrow(SongListDataBase._ID));
                    String name = c.getString(c.getColumnIndexOrThrow(SongListDataBase.SONG_LIST_NAME));
                    int count = c.getInt(c.getColumnIndexOrThrow(SongListDataBase.SONG_LIST_COUNT));

                    SongList list = new SongList();
                    list.setId(songlistId);
                    list.setName(name);
                    list.setCount(count);
                    return list;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (c != null) c.close();
                if (database != null) database.close();
            }
            return null;
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

    /**
     * 根据歌单id和歌曲名称删除歌曲
     *
     * @param songListId
     * @param name
     * @return
     */
    public boolean deleteSongBySongListIdAndName(int songListId, String name) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "delete from " + SongListDataBase.SONG_TABLE + " where "
                        + SongListDataBase.SONG_LIST_ID + "=? and "
                        + SongListDataBase.SONG_NAME + "=?";
                database.execSQL(sql, new Object[]{songListId, name});
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (database != null) database.close();
            }
            return false;
        }
    }


    /**
     * 更新songList中的count列
     *
     * @param list
     * @return
     */
    public boolean updateCountBySongList(SongList list) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "update " + SongListDataBase.SONG_LIST_TABLE
                        + " set " + SongListDataBase.SONG_LIST_COUNT + "=?"
                        + " where " + SongListDataBase._ID + "=?";
                database.execSQL(sql, new Object[]{list.getCount(), list.getId()});
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (database != null) database.close();
            }
            return false;
        }
    }


    /**
     * 判断是否存在同一路径的歌曲在同一个歌单中
     *
     * @param songListId
     * @param musicPath
     * @return
     */
    public boolean isExistsSamePathOfSongList(int songListId, String musicPath) {
        ArrayList<Song> songs = getSongByListId(songListId);

        if (songs == null || songs.size() == 0) {
            return false;
        }

        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getSongPath().equals(musicPath)) {
                Log.d("TAG", "songs.get(i).getSongPath()==="
                        + songs.get(i).getSongPath()
                        + ", musicPath===" + musicPath);
                return true;
            }
        }
        return false;
    }


    /**
     * 根据音乐文件路径和歌单id删除歌单下的歌曲
     *
     * @param path
     * @return
     */
    public boolean delSongOfSongListByPath(String path) {
        synchronized (mDataBase) {
            SQLiteDatabase database = null;
            try {
                database = mDataBase.getReadableDatabase();
                String sql = "delete from " + SongListDataBase.SONG_TABLE + " where "
                        + SongListDataBase.SONG_PATH + "=?";
                database.execSQL(sql, new Object[]{path});
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (database != null) database.close();
            }
            return false;
        }
    }

    /**
     * 检查所有歌单中匹配的音乐资源
     * 由于同一个歌单中只能存在一种路径的歌，在添加时做了判断
     * 这时，当用户删除该音乐资源时，相应删除歌单表中相应数据即可
     *
     * @param info
     * @return
     */
    public ArrayList<SongList> deleteSongBySongList(MusicInfo info) {
        // bug 删除歌曲时，会将歌单中的所有路径同民歌曲删除？？？
        ArrayList<SongList> songLists = getAllSongList();
        ArrayList<SongList> newSongLists = new ArrayList<SongList>();
        if (songLists == null || songLists.size() == 0)
            return null;

        for (int i = 0; i < songLists.size(); i++) {
            SongList list = songLists.get(i);

            ArrayList<Song> songs = getSongByListId(list.getId());
            for (int j = 0; j < songs.size(); j++) {
                Song song = songs.get(j);
                // 遍历所有歌单下的歌曲，如果匹配有则删除
                if (song.getSongPath().equals(info.getMusicPath())) {
                    boolean isDel = delSongOfSongListByPath(info.getMusicPath());
                    if (isDel) {
                        Log.d("TAG", list.getName() + "删除了" + info.getMusicName());
                        Log.d("TAG", "song.getSongPath()===" + song.getSongPath()
                                + ", info.getMusicName()===" + info.getMusicName());
                    }
                    // 获取已经删除歌单的SongList列表
                    newSongLists.add(list);
                }
            }
        }
        return newSongLists;
    }
}
