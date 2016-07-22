package com.android.app;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

public class MusicTracksActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListView;
    //    private SideBar sb_navigation_bar;
    private Cursor cursor;
    private MusicUtils.ServiceToken token;

    private ContentAdapter mAdapter;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> infos;


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

        mAdapter = new ContentAdapter(this, mItems, true);
        super.setSongCount(mAdapter.getCount());

        mAdapter.setMusicInfos(infos);

        mListView.setAdapter(mAdapter);

        super.setVisiblePlayMode(true);
        super.setSongCount(mAdapter.getCount());

    }


    @Override
    public void onCreateData() {


        infos = MusicUtils.getMusicInfo(this, false);

        for (int i = 0; i < infos.size(); i++) {

            MusicInfo info = infos.get(i);

            String musicName = info.getMusicName();
            String musicSinger = info.getSinger();

            Bitmap bitmap = info.getMusicAlbumsImage();

            ContentItem item;
            if (bitmap != null) {
                item = new ContentItem(bitmap, R.drawable.more_title_selected, musicName, musicSinger);
            } else {
                item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, musicName, musicSinger);
            }
            mItems.add(item);
        }

    }


    @Override
    public void onSearchTextChanged(String text) {
        Log.d("TAG1", text);
        String selection = MediaStore.Audio.Media.TITLE + " like '%" + text + "%'";
        infos = MusicUtils.getMusicInfo(this,selection,null,false);
        if(infos==null || infos.size()==0){
            return;
        }
        Log.d("TAG1", ""+infos.size());
    }

    @Override
    public void onSearchSubmit(String text) {
        Log.d("TAG1", text);
    }


    @Override
    public void onConvertViewClicked(int position) {
        super.playCursor(infos, false, position);

    }


}
