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

public class MusicAlbumsActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListview;
    private List<ContentItem> items = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private List<Song> songs;
    private ProgressBar mProgressBar;


    private LoadingDataTask.OnLoadDataListener<ContentItem> mListener =
            new LoadingDataTask.OnLoadDataListener<ContentItem>() {
                @Override
                public List<ContentItem> onLoadingData() {
                    List<Song> songs = MusicUtils.getAllAlbums(MusicAlbumsActivity.this);
                    ArrayList<ContentItem> items = new ArrayList<ContentItem>();
                    for (int i = 0; i < songs.size(); i++) {
                        Song song = songs.get(i);
                        // 根据专辑id获取所有该专辑下的音乐信息
                        MusicInfo info = MusicUtils.getMusicInfoByArgs(MusicAlbumsActivity.this
                                , false, MediaStore.Audio.Media.ALBUM_ID + " =?"
                                , new String[]{String.valueOf(song.getAlbumId())});
                        Bitmap bitmap = MusicUtils.getArtwork(MusicAlbumsActivity.this
                                , info.getMusicId(), info.getAlbumId());
                        String AlbumsName = song.getAlbumName();
                        int AlbumMusicCount = MusicUtils.getSongListForAlbum(MusicAlbumsActivity.this
                                , info.getAlbumId()).length;
                        ContentItem item = new ContentItem(bitmap, R.drawable.c_right, AlbumsName, AlbumMusicCount + "首");
                        items.add(item);
                    }

                    return items;
                }

                @Override
                public void onLoadDataSuccess(List<ContentItem> mParams) {
                    mProgressBar.setVisibility(View.GONE);
                    if (mParams == null || mParams.size() == 0) {
                        Toast.makeText(MusicAlbumsActivity.this
                                , "没有发现任何专辑", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    items = mParams;
                    mAdapter = new ContentAdapter(MusicAlbumsActivity.this, items, false);
                    mListview.setAdapter(mAdapter);
                }

                @Override
                public void onLoadDataFail(String msg) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MusicAlbumsActivity.this
                            , msg, Toast.LENGTH_SHORT).show();
                }
            };

    @Override
    public void onInitView() {
        setContentView(R.layout.music_albums_layout);

    }

    @Override
    public void onCreateView() {
        super.setVisiblePlayMode(false);
        super.setTitleText("Album");
        mListview = (ListView) findViewById(R.id.lv_music);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreateData() {
        songs = MusicUtils.getAllAlbums(MusicAlbumsActivity.this);
        LoadingDataTask<ContentItem> mTask = new LoadingDataTask<ContentItem>(mListener);
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
        super.removeAllMsg();
        Song song = songs.get(position);
        Intent intent = new Intent(MusicAlbumsActivity.this, MusicAlbumsContentActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AlbumsId", song.getAlbumId());
        startActivity(intent);
    }

}
