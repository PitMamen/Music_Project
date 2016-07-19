package com.android.app;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MusicRecentActivity extends BaseActivity {


    private ArrayList<MusicInfo> mMusicInfos = new ArrayList<MusicInfo>();
    private List<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;


    @Override
    public void onCreateView() {
        mListView = (ListView) findViewById(R.id.lv_recent_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(mMusicInfos);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_music_recent);
    }

    @Override
    public void onCreateData() {
        ArrayList<Song> songs = DataBaseManager.getInstance(this).getAllRecentSong();
        if (songs == null || songs.size() == 0) {
            Toast.makeText(this, "最近播放还没有音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);

            String selection = MediaStore.Audio.Media._ID + "=?";
            String[] selectionArgs = {String.valueOf(song.getId())};

            MusicInfo info = MusicUtils.getMusicInfoByArgs(this, false, selection, selectionArgs);
            mMusicInfos.add(info);

            Bitmap bm = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId(), true);

            ContentItem item = new ContentItem(bm
                    , R.drawable.more_title_selected
                    , song.getName()
                    , song.getSinger());
            mItems.add(item);

        }
    }

    @Override
    public void update(Observable observable, Object data) {


        super.update(observable, data);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
