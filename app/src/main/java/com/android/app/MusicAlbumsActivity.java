package com.android.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicAlbumsActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked, ContentAdapter.OnOperateClicked {

    private ListView mListview;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;
    private ArrayList<MusicInfo> arrayList;

    private Cursor cursor;

    private MusicUtils.ServiceToken token;


    @Override
    public void onInitView() {
        setContentView(R.layout.music_albums_layout);

    }

    @Override
    public void onCreateView() {
        super.setTitleText("Album");
        mListview = (ListView) findViewById(R.id.lv_music_detail);
        mListview.setAdapter(new ContentAdapter(this, items, false));
        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
    }


    @Override
    public void onCreateData() {


        arrayList = MusicUtils.getMusicInfo(this, false);
        String albumName = "";
        for (int i = 0; i < arrayList.size(); i++) {

            MusicInfo info = arrayList.get(i);
            Log.d("TAG", info.toString());

            Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId(), true);
            long[] songsIds = MusicUtils.getSongListForAlbum(this, info.getAlbumId());

            if (info.getMusicAlbumsName().equals(albumName)) {
                continue;
            }
            ContentItem item = new ContentItem(bitmap, R.drawable.c_right
                    , info.getMusicAlbumsName(), songsIds.length + "首");


            albumName = info.getMusicAlbumsName();
            items.add(item);
        }


    }


    @Override
    public void onOperateClicked(int position, View v) {

        Intent intent = new Intent(MusicAlbumsActivity.this, MediaPlaybackActivity.class);
        startActivity(intent);


    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }




    @Override
    public void onConvertViewClicked(int position) {
        super.playCursor(arrayList,false,position);



    }

}
