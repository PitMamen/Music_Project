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
public class MusicSingerActivity extends  BaseActivity{

    private ListView mListview;
    private ContentAdapter mAdapter;
    private SideBar sb_navigation_bar;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();







    @Override
    public void onInitView() {

        setContentView(R.layout.music_singer_layout);

    }
    @Override
    public void onCreateView() {

        super.setTitleText("Singer");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        mListview = (ListView) findViewById(R.id.lv_music_detail);

        mListview.setAdapter(new ContentAdapter(this,items,false));

        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);

    }






    @Override
    public void onCreateData() {

        MusicUtils.getMusicInfo(this,false, new MusicUtils.OnMusicLoadedListener() {
            @Override
            public void onMusicLoadSuccess(ArrayList<MusicInfo> infos) {

                for (int i = 0; i < infos.size(); i++) {
                    MusicInfo info = infos.get(i);
                    String singer = info.getSinger();
                    int albumsmusicNumber = info.getMusicAlbumsNumber();
                    Bitmap bitmap = info.getMusicAlbumsImage();
                    ContentItem item;
                    if (bitmap != null) {
                        item = new ContentItem(bitmap, R.drawable.more_title_selected, singer, albumsmusicNumber + "首");
                    } else {
                        item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, singer, albumsmusicNumber + "首");
                    }
                    items.add(item);
                }

                }


            @Override
            public void onMusicLoading() {

            }

            @Override
            public void onMusicLoadFail() {

            }
        });




    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
