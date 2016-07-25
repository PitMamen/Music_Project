package com.android.app;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.handler.LoadingDataTask;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class LastUpdatedActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private List<ContentItem> mItems = new ArrayList<ContentItem>();
    private List<MusicInfo> infos = new ArrayList<MusicInfo>();
    private ProgressBar mProgress;

    private LoadingDataTask.OnLoadDataListener<MusicInfo> mListener =
            new LoadingDataTask.OnLoadDataListener<MusicInfo>() {
                @Override
                public List<MusicInfo> onLoadingData() {
                    return MusicUtils.getMusicInfo(LastUpdatedActivity.this, true);
                }

                @Override
                public void onLoadDataSuccess(List<MusicInfo> mParams) {
                    mProgress.setVisibility(View.GONE);
                    if (mParams == null || mParams.size() == 0) {
                        return;
                    }
                    infos = mParams;
                    for (int i = 0; i < infos.size(); i++) {
                        MusicInfo info = infos.get(i);
                        String name = info.getMusicName();
                        String singer = info.getSinger();
                        Bitmap bitmap = MusicUtils.getArtwork(LastUpdatedActivity.this
                                , info.getMusicId(), info.getAlbumId());
                        ContentItem item = new ContentItem(bitmap
                                , R.drawable.more_title_selected, name, singer);
                        mItems.add(item);
                    }
                    mAdapter = new ContentAdapter(LastUpdatedActivity.this, mItems, true);
                    mAdapter.setMusicInfos(infos);
                    mListView.setAdapter(mAdapter);
                    LastUpdatedActivity.this.setVisiblePlayMode(true); // 是否显示play mode 状态栏
                    LastUpdatedActivity.this.setSongCount(mAdapter.getCount()); // 设置状态上的数字
                }

                @Override
                public void onLoadDataFail(String msg) {
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(LastUpdatedActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            };


    @Override
    public void onCreateView() {
        getWindow().setBackgroundDrawable(getDrawable(android.R.color.white));
        super.setTitleText("最近更新");
        mListView = (ListView) findViewById(R.id.lv_music);
        mProgress = (ProgressBar) findViewById(R.id.loading);
        mProgress.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInitView() {
        setContentView(R.layout.activity_last_updated);
    }

    @Override
    public void onCreateData() {
        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, false);
        LoadingDataTask<MusicInfo> mTask = new LoadingDataTask<MusicInfo>(mListener);
        mTask.doInBackGround();
    }


    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }


    @Override
    public void onConvertViewClicked(int position) {
        super.playCursor(infos, true, position);
    }


}
