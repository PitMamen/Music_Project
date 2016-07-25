package com.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.handler.LoadingDataTask;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicSingerActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListview;
    private ContentAdapter madapter;
    private List<ContentItem> items = new ArrayList<ContentItem>();
    private ArrayList<Song> songs;
    private ProgressBar mProgressBar;

    private LoadingDataTask.OnLoadDataListener<ContentItem> mListener
            = new LoadingDataTask.OnLoadDataListener<ContentItem>() {
        @Override
        public List<ContentItem> onLoadingData() {
            ArrayList<Song> songs = MusicUtils.getAllArtist(MusicSingerActivity.this);
            if (songs == null || songs.size() == 0) {
                return null;
            }
            ArrayList<ContentItem> items = new ArrayList<ContentItem>();

            for (int i = 0; i < songs.size(); i++) {
                // 获取所有歌手的id
                Song song = songs.get(i);
//            Log.d("haha", "歌手 id ====" + song.getArtistId());
//            Log.d("haha", "歌手 ====" + song.getSinger());
                // 根据歌手id获取所有该歌手的音乐信息
                MusicInfo info = MusicUtils.getMusicInfoByArgs(MusicSingerActivity.this
                        , false, MediaStore.Audio.Media.ARTIST_ID + " =?"
                        , new String[]{String.valueOf(song.getArtistId())});

                Bitmap bm = MusicUtils.getArtwork(MusicSingerActivity.this
                        , info.getMusicId(), info.getAlbumId());

                String singername = song.getSinger();
                int musicCount = MusicUtils.getSongListForArtist(MusicSingerActivity.this
                        , song.getArtistId()).length;

                ContentItem item = new ContentItem(bm, R.drawable.c_right, singername, musicCount + "首");

                items.add(item);
            }
            return items;
        }

        @Override
        public void onLoadDataSuccess(List<ContentItem> mParams) {
            mProgressBar.setVisibility(View.GONE);

            if (mParams == null || mParams.size() == 0) {
                return;
            }
            items = mParams;
            madapter = new ContentAdapter(MusicSingerActivity.this, items, false);
            mListview.setAdapter(madapter);
            MusicSingerActivity.this.setVisiblePlayMode(true);
            MusicSingerActivity.this.setSongCount(madapter.getCount());
        }

        @Override
        public void onLoadDataFail(String msg) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MusicSingerActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onInitView() {
        setContentView(R.layout.music_singer_layout);
    }

    @Override
    public void onCreateView() {
        super.setTitleText("Singer");
        mListview = (ListView) findViewById(R.id.lv_music);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onCreateData() {
        songs = MusicUtils.getAllArtist(this);
        new LoadingDataTask<ContentItem>(mListener).doInBackGround();
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }

    @Override
    public void onConvertViewClicked(int position) {
        super.removeAllMsg();
        Song song = songs.get(position);
        Intent intent = new Intent(MusicSingerActivity.this, MusicSingerContentActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("artistId", song.getArtistId());
        startActivity(intent);


    }
}


