package com.android.app;

import android.webkit.WebView;
import android.widget.ListView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicSingerActivity extends  BaseActivity{

    private ListView mListview;
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;







    @Override
    public void onInitView() {

        setContentView(R.layout.music_singer_layout);

    }
    @Override
    public void onCreateView() {

        super.setTitleText("歌手");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        mListview = (ListView) findViewById(R.id.lv_music_detail);

    }


    @Override
    public void onCreateData() {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
