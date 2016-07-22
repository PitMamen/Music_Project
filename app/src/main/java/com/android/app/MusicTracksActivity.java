package com.android.app;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.TextUtils;
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

public class MusicTracksActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private List<MusicInfo> arrayList = new ArrayList<MusicInfo>();
    private ProgressBar mLoadingProgressBar;
    private String mText;


    @Override
    public void onInitView() {
        setContentView(R.layout.music_tracks_layout);

    }

    //初始化视图
    @Override
    public void onCreateView() {
        super.setVisiblePlayMode(true);
        super.setTitleText("Music");
        mListView = (ListView) findViewById(R.id.lv_music_detail);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }


    // 初始化数据
    @Override
    public void onCreateData() {
        selectAllSongs();
    }


    /**
     * 数据第一次加载的监听
     */
    private LoadingDataTask.OnLoadDataListener<MusicInfo> mLoadDataListener
            = new LoadingDataTask.OnLoadDataListener<MusicInfo>() {

        @Override
        public List<MusicInfo> onLoadingData() {
            return MusicUtils.getMusicInfo(MusicTracksActivity.this, false);
        }

        @Override
        public void onLoadDataSuccess(List<MusicInfo> mParams) {
            mLoadingProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (mParams == null || mParams.size() == 0) {
                return;
            }
            arrayList = mParams;
            createItems();
            mAdapter = new ContentAdapter(MusicTracksActivity.this, mItems, true);
            mAdapter.setMusicInfos(arrayList);
            mListView.setAdapter(mAdapter);
            MusicTracksActivity.this.setVisiblePlayMode(true);
            MusicTracksActivity.this.setSongCount(mAdapter.getCount());

        }

        @Override
        public void onLoadDataFail(String msg) {
            mLoadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(MusicTracksActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 数据更新时的监听
     */
    private LoadingDataTask.OnLoadDataListener<MusicInfo> mUpdateDataListener
            = new LoadingDataTask.OnLoadDataListener<MusicInfo>() {

        @Override
        public List<MusicInfo> onLoadingData() {
            if (TextUtils.isEmpty(mText.trim()))
                return null;
            String selection = MediaStore.Audio.Media.TITLE + " like '%" + mText + "%'";
            return MusicUtils.getMusicInfo(MusicTracksActivity.this
                    , selection, null, false);
        }

        @Override
        public void onLoadDataSuccess(List<MusicInfo> mParams) {
            mLoadingProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (mParams == null) {
                return;
            }
            arrayList = mParams;
            createItems();
            mAdapter.setMusicInfos(arrayList);
            mAdapter.notifyDataSetChanged();
            MusicTracksActivity.this.setSongCount(mAdapter.getCount());
        }


        @Override
        public void onLoadDataFail(String msg) {
            mLoadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(MusicTracksActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 创建一个item
     */
    private void createItems() {
        mItems.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            MusicInfo info = arrayList.get(i);
            Bitmap bm = MusicUtils.getArtwork(MusicTracksActivity.this
                    , info.getMusicId(), info.getAlbumId(), true);


            ContentItem item = new ContentItem(bm, R.drawable.more_title_selected
                    , info.getMusicName(), info.getSinger());

            mItems.add(item);
        }
    }

    /**
     * 查询获取所有歌曲
     */

    private void selectAllSongs() {
        LoadingDataTask<MusicInfo> mTask = new LoadingDataTask<MusicInfo>(mLoadDataListener);
        mTask.doInBackGround();
    }


    @Override
    public void onSearchTextChanged(String text) {
        mText = text;
        searchSong(text);

    }


    @Override
    public void onSearchSubmit(String text) {
        mText = text;
        searchSong(text);
    }

    /**
     * 根据文本内容查询歌曲信息
     *
     * @param text
     */
    private void searchSong(String text) {
        if (mLoadingProgressBar != null) {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(text.trim())) {
            selectAllSongs();
            return;
        }
        LoadingDataTask<MusicInfo> mTask = new LoadingDataTask<MusicInfo>(mUpdateDataListener);
        mTask.doInBackGround();
//        String selection = MediaStore.Audio.Media.TITLE + " like '%" + text + "%'";
//
//
//        List<MusicInfo> newList = MusicUtils.getMusicInfo(this, selection, null, false);
//        if (newList == null || newList.size() == 0) {
//            return;
//        }
//        for (int i = 0; i < newList.size(); i++) {
//            MusicInfo info = newList.get(i);
//
//            Bitmap bm = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId(), true);
//
//            ContentItem item = new ContentItem(bm, R.drawable.more_title_selected
//                    , info.getMusicName(), info.getSinger());
//
//            mItems.add(item);
//        }
//        mAdapter.notifyDataSetChanged();
//        super.setSongCount(mAdapter.getCount());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConvertViewClicked(int position) {
        super.playCursor(arrayList, false, position);

    }


}
