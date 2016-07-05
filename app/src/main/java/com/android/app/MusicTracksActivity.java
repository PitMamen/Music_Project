package com.android.app;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxinkai001 on 2016/6/23.
 */
public class MusicTracksActivity extends BaseActivity {

    private ImageButton ib_music_back;
    private TextView tv_title, tv_paly_mode, tv_music_number;
    private ImageView iv_music_search, iv_music_icon;
    private ListView lv_music_detail;
    private SideBar sb_navigation_bar;

    private ContentAdapter mAdapter;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();


    @Override
    public void onInitView() {
        setContentView(R.layout.music_tracks_layout);

    }

    //初始化视图
    @Override
    public void onCreateView() {
        super.setTitleText("曲目");

        iv_music_icon = (ImageView) findViewById(R.id.iv_mode_icon);

        tv_paly_mode = (TextView) findViewById(R.id.tv_play_mode);

        tv_music_number = (TextView) findViewById(R.id.tv_music_num);


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        lv_music_detail = (ListView) findViewById(R.id.lv_music_detail);
        mAdapter = new ContentAdapter(this, mItems);
        lv_music_detail.setAdapter(mAdapter);


        int count = mAdapter.getCount();

        tv_music_number.setText(count + "首");


        sb_navigation_bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int position, String selectStr) {




            }
        });


    }


    @Override
    public void onCreateData() {

        MusicUtils.getMusicInfo(this, new MusicUtils.OnMusicLoadedListener() {
            @Override
            public void onMusicLoadSuccess(ArrayList<MusicInfo> infos) {


                for (int i = 0; i < infos.size(); i++) {

                    Log.i("TAG", "infos.size=====" + infos.size());

                    String musicname = infos.get(i).getMusicName();
                    String singer = infos.get(i).getSinger();

                    ContentItem items = new ContentItem(R.drawable.app_music, R.drawable.ic_menu_eq, musicname, singer);

                    mItems.add(items);

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
