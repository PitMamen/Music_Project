package com.android.app;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

public class MusicTracksActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView lv_music_detail;
//    private SideBar sb_navigation_bar;
    private Cursor cursor;
    private MusicUtils.ServiceToken token;

    private ContentAdapter mAdapter;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> arrayList;


    @Override
    public void onInitView() {
        setContentView(R.layout.music_tracks_layout);

    }

    //初始化视图
    @Override
    public void onCreateView() {
        super.setVisiblePlayMode(true);
        super.setTitleText("Music");


        lv_music_detail = (ListView) findViewById(R.id.lv_music_detail);

        mAdapter = new ContentAdapter(this, mItems, true);

        mAdapter.setMusicInfos(arrayList);

        lv_music_detail.setAdapter(mAdapter);

        int count = mAdapter.getCount();







    }


    @Override
    public void onCreateData() {


        arrayList = MusicUtils.getMusicInfo(this, false);

        for (int i = 0; i < arrayList.size(); i++) {

            MusicInfo info = arrayList.get(i);

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
        Log.d("TAG1",text);



    }

    @Override
    public void onSearchSubmit(String text) {
        Log.d("TAG1",text);
    }



    @Override
    public void onConvertViewClicked(int position) {

        super.playCursor(arrayList, false, position);

    }



}
