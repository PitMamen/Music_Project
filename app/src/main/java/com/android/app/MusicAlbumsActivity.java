package com.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;

public class MusicAlbumsActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListview;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;


    private ArrayList<Song> songs;
    private MusicInfo info;


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
        if (songs == null || songs.size() == 0) {
            return;
        }

        for (int i = 0; i < songs.size(); i++) {

            Song song = songs.get(i);

            Log.d("haha", "专辑 id ====" + song.getAlbumId());
            Log.d("haha", "专辑名==== " + song.getAlbumName());
            // 根据歌手id获取所有该歌手的音乐信息
            info = MusicUtils.getMusicInfoByArgs(this, false, MediaStore.Audio.Media.ALBUM_ID + " =?"
                    , new String[]{String.valueOf(song.getAlbumId())});

//            File mFile = new File(info.getMusicPath());
//            mFile  = new File(mFile.getParent());
//            String name = mFile.getName();
//            if(name.equals(song.getAlbumName())){
//                continue;
//            }

            Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId());
            String AlbumsName = song.getAlbumName();
            int AlbumMusicCount = MusicUtils.getSongListForAlbum(this,info.getAlbumId()).length;
           // int count = info.getMusicAlbumsNumber();
            ContentItem item = new ContentItem(bitmap,R.drawable.c_right,AlbumsName,AlbumMusicCount+"首");

            items.add(item);

        }

    }



    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }


    @Override
    public void onConvertViewClicked(int position) {
        Song song = songs.get(position);
        Intent intent = new Intent(MusicAlbumsActivity.this, MusicAlbumsContentActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AlbumsId",song.getAlbumId());
        startActivity(intent);


    }

}
