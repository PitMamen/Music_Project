package com.android.app;

import android.graphics.Bitmap;
import android.widget.ListView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicAlbumsActivity extends BaseActivity {

    private ListView mListview;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;
    private ArrayList<MusicInfo> arrayList;


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

        for (int i = 0; i < arrayList.size(); i++) {

            MusicInfo info = arrayList.get(i);

            String musicAlbums = info.getMusicAlbumsName();
            int musicAblumscount = info.getMusicAlbumsNumber();

            Bitmap bitmap = info.getMusicAlbumsImage();
            ContentItem item;
            if (bitmap != null) {

                item = new ContentItem(bitmap, R.drawable.more_title_selected, musicAlbums, musicAblumscount + "首");

            } else {
                item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, musicAlbums, musicAblumscount + "首");
            }

            items.add(item);

        }


    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
