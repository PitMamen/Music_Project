package com.android.app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.handler.LoadingDataTask;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxinkai001 on 2016/7/21.
 */
public class MusicSingerContentActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {


    private ListView mlistview;
    private ContentAdapter madapter;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private List<MusicInfo> infos;
    private ProgressBar mProgressBar;

    private LoadingDataTask.OnLoadDataListener<MusicInfo> mListener
            = new LoadingDataTask.OnLoadDataListener<MusicInfo>() {

        @Override
        public List<MusicInfo> onLoadingData() {
            Intent intent = getIntent();
            long artistId = intent.getLongExtra("artistId", 0L);
            //根据歌手id 查询歌曲信息
            return MusicUtils.getMusicInfo(MusicSingerContentActivity.this
                    , MediaStore.Audio.Media.ARTIST_ID + " =?"
                    , new String[]{String.valueOf(artistId)}, false);

        }

        @Override
        public void onLoadDataSuccess(List<MusicInfo> mParams) {
            mProgressBar.setVisibility(View.GONE);
            if (mParams == null || mParams.size() == 0) return;
            infos = mParams;

            for (int i = 0; i < infos.size(); i++) {
                MusicInfo info = infos.get(i);
                Log.d("haha", "info===" + info.getSinger() + "========" + info.getMusicName());

                Bitmap bitmap = MusicUtils.getArtwork(MusicSingerContentActivity.this
                        , info.getMusicId(), info.getAlbumId());
                ContentItem item = new ContentItem(bitmap, R.drawable.more_title_selected
                        , info.getMusicName(), info.getSinger());

                items.add(item);
            }
            madapter = new ContentAdapter(MusicSingerContentActivity.this, items, true);
            mlistview.setAdapter(madapter);
            madapter.setMusicInfos(infos);
            MusicSingerContentActivity.this.setVisiblePlayMode(true);
            MusicSingerContentActivity.this.setSongCount(madapter.getCount());

        }

        @Override
        public void onLoadDataFail(String msg) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MusicSingerContentActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onInitView() {
        setContentView(R.layout.singer_detail);
    }


    @Override
    public void onCreateView() {
        super.setTitleText("歌手详情");
        mlistview = (ListView) findViewById(R.id.lv_music);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateData() {
        // 启动线程加载数据
        new LoadingDataTask<MusicInfo>(mListener).doInBackGround();

//        Intent intent = getIntent();
//        long artistId = intent.getLongExtra("artistId", 0L);
//        //根据歌手id 查询歌曲信息
//        infos = MusicUtils.getMusicInfo(this, MediaStore.Audio.Media.ARTIST_ID + " =?"
//                , new String[]{String.valueOf(artistId)}, false);
//        for (int i = 0; i < infos.size(); i++) {
//            MusicInfo info = infos.get(i);
//            Log.d("haha", "info===" + info.getSinger() + "========" + info.getMusicName());
//
//            Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId());
//
//            ContentItem item = new ContentItem(bitmap, R.drawable.more_title_selected, info.getMusicName(), info.getSinger());
//
//            items.add(item);
//        }

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
