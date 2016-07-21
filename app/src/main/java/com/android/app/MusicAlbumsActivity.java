package com.android.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MusicAlbumsActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked, ContentAdapter.OnOperateClicked {

    private ListView mListview;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;
    private ArrayList<MusicInfo> arrayList;


    private ArrayList<Song> songs;
    private  MusicInfo info;


    @Override
    public void onInitView() {
        setContentView(R.layout.music_albums_layout);

    }

    @Override
    public void onCreateView() {
        super.setVisiblePlayMode(false);
        super.setTitleText("Album");
        mListview = (ListView) findViewById(R.id.lv_music_detail);
        mListview.setAdapter(new ContentAdapter(this, items, false));
        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
    }


    @Override
    public void onCreateData() {


        songs = MusicUtils.getAllAlbums(this);


        for (int i = 0; i < songs.size(); i++) {
            // 获取所有歌手的id
            Song song = songs.get(i);

            Log.d("haha", "專輯 id ====" + song.getAlbumId());
         //   Log.d("haha", "歌手 ====" + song.getSinger());
            // 根据歌手id获取所有该歌手的音乐信息
            info = MusicUtils.getMusicInfoByArgs(this, false, MediaStore.Audio.Media.ALBUM_ID + " =?"
                    , new String[]{String.valueOf(song.getAlbumId())});

          /*  Log.d("HaHa", "專輯==="+info.getMusicAlbumsName()+"歌曲==="+info.getMusicAlbumsNumber());



//            Bitmap bm = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId());

            String singername = song.getSinger();
            int musicCount = MusicUtils.getSongListForArtist(this,song.getAlbumId()).length;

            ContentItem item = new ContentItem(R.drawable.singer, R.drawable.c_right, singername, musicCount + "首");

            items.add(item);*/


        }

















        /*arrayList = MusicUtils.getMusicInfo(this, true);

        String albumName = null;
        for (int i = 0; i < arrayList.size(); i++) {

            MusicInfo info = arrayList.get(i);
            Log.d("TAG", info.toString());

            Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId(), true);
            long[] songsIds = MusicUtils.getSongListForAlbum(this, info.getAlbumId());

            if (info.getMusicAlbumsName().equals(albumName)) {
                continue;
            }
            ContentItem item = null;

            if (bitmap != null) {
                item = new ContentItem(bitmap, R.drawable.c_right
                        , info.getMusicAlbumsName(), songsIds.length + "首");
            } else {
                item = new ContentItem(R.drawable.singer, R.drawable.c_right, info.getMusicAlbumsName(), songsIds.length + "首");
            }
            albumName = info.getMusicAlbumsName();
            items.add(item);


        }*/


    }


    @Override
    public void onOperateClicked(int position, View v) {

        Intent intent = new Intent(MusicAlbumsActivity.this, MediaPlaybackActivity.class);
        startActivity(intent);


    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }




    @Override
    public void onConvertViewClicked(int position) {
        super.playCursor(arrayList, false, position);


    }

}
