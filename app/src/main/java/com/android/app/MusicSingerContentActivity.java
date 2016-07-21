package com.android.app;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/7/21.
 */
public class MusicSingerContentActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {

    private TextView tv_music_count, tv_play_model;
    private ImageView iv_play_modle;

    private ListView mlistview;

    private ContentAdapter madapter;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> arrayList;

    private ContentItem item;
    private  MusicInfo info;


    @Override
    public void onInitView() {
        setContentView(R.layout.singer_detail);
    }


    @Override
    public void onCreateView() {
        super.setTitleText("歌手详情");
        mlistview = (ListView) findViewById(R.id.lv_singer_datail);

        madapter = new ContentAdapter(this, items, true);

        madapter.setMusicInfos(arrayList);
        mlistview.setAdapter(madapter);

        View headview = getLayoutInflater().inflate(R.layout.listview_head_layout, null);
        tv_music_count = (TextView) headview.findViewById(R.id.tv_song_count);
        tv_play_model = (TextView) headview.findViewById(R.id.tv_play_mode);
        iv_play_modle = (ImageView) headview.findViewById(R.id.play_mode_icon);


      /*  mlistview.addView(headview);

        tv_music_count.setText(String.valueOf(madapter.getCount()));*/


    }

    @Override
    public void onCreateData() {


        Intent intent = getIntent();

        String StringE = intent.getStringExtra("artistId");
        Song song = new Song();

        info = MusicUtils.getMusicInfoByArgs(this, false, MediaStore.Audio.Media.ARTIST_ID + " =?"
                , new String[]{String.valueOf(song.getArtistId())});


        arrayList = MusicUtils.getMusicInfo(this,false,);



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
