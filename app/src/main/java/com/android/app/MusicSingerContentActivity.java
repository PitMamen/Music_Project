package com.android.app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/7/21.
 */
public class MusicSingerContentActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {


    private ListView mlistview;

    private ContentAdapter madapter;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();

    private ArrayList<MusicInfo> infos;


    @Override
    public void onInitView() {
        setContentView(R.layout.singer_detail);
    }


    @Override
    public void onCreateView() {
        super.setTitleText("歌手详情");
        mlistview = (ListView) findViewById(R.id.lv_singer_datail);

        madapter = new ContentAdapter(this, items, true);

        mlistview.setAdapter(madapter);

        super.setVisiblePlayMode(true);
        super.setSongCount(madapter.getCount());


    }

    @Override
    public void onCreateData() {
        Intent intent = getIntent();
        long artistId = intent.getLongExtra("artistId", 0L);
        infos = MusicUtils.getMusicInfo(this, MediaStore.Audio.Media.ARTIST_ID + " =?"
                , new String[]{String.valueOf(artistId)}, false);
        for (int i = 0; i < infos.size(); i++) {
            MusicInfo info = infos.get(i);
            Log.d("haha", "info===" + info.getSinger() + "========" + info.getMusicName());

            Bitmap bitmap = MusicUtils.getArtwork(this,info.getMusicId(),info.getAlbumId());

            ContentItem item = new ContentItem(bitmap, R.drawable.more_title_selected, info.getMusicName(), info.getSinger());

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

        super.playCursor(infos, false, position);


    }

}
