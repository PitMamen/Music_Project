package com.android.app;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.handler.LoadingDataTask;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicRecentActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {


    private List<MusicInfo> mMusicInfos = new ArrayList<MusicInfo>();
    private List<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ProgressBar mProgressBar;

    private LoadingDataTask.OnLoadDataListener<MusicInfo> mListener
            = new LoadingDataTask.OnLoadDataListener<MusicInfo>() {
        @Override
        public List<MusicInfo> onLoadingData() {
            ArrayList<Song> songs = DataBaseManager
                    .getInstance(MusicRecentActivity.this).getAllRecentSong();
            if (songs == null || songs.size() == 0) {
                return null;
            }

            String[] selectionArgs = new String[songs.size()];
            StringBuilder selection = new StringBuilder();
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                selection.append(MediaStore.Audio.Media._ID + "=?");
                selection.append(i == songs.size() - 1 ? "" : " or ");
                selectionArgs[i] = String.valueOf(song.getId());
            }
            return MusicUtils.getMusicInfo(MusicRecentActivity.this
                    , selection.toString(), selectionArgs, false);
        }

        @Override
        public void onLoadDataSuccess(List<MusicInfo> mParams) {
            mProgressBar.setVisibility(View.GONE);
            if (mParams == null || mParams.size() == 0) {
                Toast.makeText(MusicRecentActivity.this, "先听些歌曲吧！", Toast.LENGTH_SHORT).show();
                return;
            }
            mMusicInfos  = mParams;
            for (int i = 0; i < mMusicInfos.size(); i++) {
                MusicInfo info = mMusicInfos.get(i);
                Bitmap bm = MusicUtils.getArtwork(MusicRecentActivity.this
                        , info.getMusicId(), info.getAlbumId(), true);

                ContentItem item = new ContentItem(bm
                        , R.drawable.more_title_selected
                        , info.getMusicName()
                        , info.getSinger());
                mItems.add(item);
            }

            mAdapter = new ContentAdapter(MusicRecentActivity.this, mItems, true);
            mAdapter.setMusicInfos(mMusicInfos);
            mListView.setAdapter(mAdapter);
            MusicRecentActivity.this.setVisiblePlayMode(true);
            MusicRecentActivity.this.setSongCount(mAdapter.getCount());
        }

        @Override
        public void onLoadDataFail(String msg) {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MusicRecentActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreateView() {
        mListView = (ListView) findViewById(R.id.lv_music);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);
        super.setTitleText("最近播放");
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_music_recent);
    }

    @Override
    public void onCreateData() {
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
        super.playCursor(mMusicInfos, false, position);
    }
}
