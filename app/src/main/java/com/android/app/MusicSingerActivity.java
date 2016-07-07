package com.android.app;

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

        super.setTitleText("singer");


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

                for (int i = 0; i <infos.size() ; i++) {
                    String singer =  infos.get(i).getSinger();
                    int albumsmusicNumber = infos.get(i).getMusicAlbumsNumber();
                    ContentItem item = new ContentItem(R.drawable.app_music,R.drawable.ic_menu_eq,singer,albumsmusicNumber+"首");
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
