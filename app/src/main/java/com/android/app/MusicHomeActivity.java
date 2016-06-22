package com.android.app;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlighttech.music.adapter.HomeAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class MusicHomeActivity extends BaseActivity implements View.OnClickListener{

    private ListView mListView;
    private List<ContentItem> mData;
    private HomeAdapter mAdapter;
    private TextView tvTracks,tvAlbums,tvArtist;

    @Override
    public void onCreateView() {
        setContentView(R.layout.music_home_page_layout);

        tvTracks = (TextView) findViewById(R.id.tv_tracks);
        tvAlbums = (TextView) findViewById(R.id.tv_album);
        tvArtist = (TextView) findViewById(R.id.tv_artist);

        tvTracks.setOnClickListener(this);
        tvAlbums.setOnClickListener(this);
        tvArtist.setOnClickListener(this);


        mListView = (ListView) findViewById(R.id.home_list_view);
        mAdapter = new HomeAdapter(this, mData);
        mListView.setAdapter(mAdapter);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<MusicInfo> infos=MusicUtils.getMusicInfo(MusicHomeActivity.this);
                for (MusicInfo info : infos) {
                    Log.d("TAG",info.toString());
                }
                mAdapter.updateItem();
            }
        }, 2000);
    }

    @Override
    public void onCreateData() {
        mData = new ArrayList<ContentItem>();
        ContentItem itemDir = new ContentItem(R.drawable.seek_thumb
                , R.drawable.left, "文件夹");
        ContentItem itemPlayList = new ContentItem(R.drawable.playlist_tile
                , R.drawable.left, "播放列表");
        ContentItem itemUpdate = new ContentItem(R.drawable.app_music
                , R.drawable.left, "最近更新");
        ContentItem itemPlay = new ContentItem(R.drawable.stat_notify_musicplayer
                , R.drawable.left, "最近播放");
        mData.add(itemDir);
        mData.add(itemPlayList);
        mData.add(itemUpdate);
        mData.add(itemPlay);
    }

    @Override
    public void onSearchTextChanged(String text) {
        Log.d("TAG", text);
    }

    @Override
    public void onSearchSubmit(String text) {
        Log.d("TAG", text);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Toast.makeText(MusicHomeActivity.this,"哈哈",Toast.LENGTH_LONG).show();
    }
}
