package com.dlighttech.music.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhujiang on 16-7-2.
 */
public class PreferencesUtils {

    private static SharedPreferences mShared;
    private static PreferencesUtils mPreferencesUtils;

    public static final String SONG_LIST_ID_KEY = "song_list_id_key";
    public static final String IS_SONG_LIST_DEL_KEY = "song_list_del_song";
    public static final String SONG_LIST = "song_list";

    public static final String MUSIC = "music";
    public static final String SINGER = "singer";
    public static final String MUSIC_NAME = "music_name";
    public static final String SONG_ID = "song_id";
    public static final String ALBUM_ID = "album_id";
    public static final String IS_PLAYING = "is_playing";
    public static final String CURR_TIME = "curr_time";


    private PreferencesUtils() {

    }

    public static PreferencesUtils getInstance(Context ctx, String name) {
        if (mShared == null) {
            mShared = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        if (mPreferencesUtils == null) {
            mPreferencesUtils = new PreferencesUtils();
        }
        return mPreferencesUtils;
    }


    public boolean putData(String key, Object data) {
        try {
            SharedPreferences.Editor editor = mShared.edit();
            if (data == null) {
                return false;
            }
            if (data instanceof Integer) {
                editor.putInt(key, (int) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (long) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (float) data);
            } else if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (boolean) data);
            }
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean getBoolean(String key) {
        return mShared.getBoolean(key, false);
    }

    public int getInteger(String key) {
        return mShared.getInt(key, 0);
    }

    public long getLong(String key) {
        return mShared.getLong(key, -1);
    }

    public float getFloat(String key) {
        return mShared.getFloat(key, 0F);
    }

    public String getStr(String key) {
        return mShared.getString(key, null);
    }

}
