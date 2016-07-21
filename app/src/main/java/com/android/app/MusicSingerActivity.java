package com.android.app;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.ListView;
import android.widget.TextView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicSingerActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListview;
    private SideBar sb_navigation_bar;
    private TextView tv_music_number;

    private ContentAdapter mdapter;
    private Cursor cursor;
    private MusicUtils.ServiceToken token;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> arrayList;

    @Override
    public void onInitView() {

        setContentView(R.layout.music_singer_layout);

    }

    @Override
    public void onCreateView() {

        super.setTitleText("Singer");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        mListview = (ListView) findViewById(R.id.lv_music_detail);
        tv_music_number = (TextView) findViewById(R.id.tv_music_num);


        mdapter = new ContentAdapter(this, items, true);
        mdapter.setMusicInfos(arrayList);
        mListview.setAdapter(mdapter);

        int count = mdapter.getCount();
        tv_music_number.setText("共" + count + "首");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);

    }

    @Override
    public void onCreateData() {
        arrayList = MusicUtils.getMusicInfo(this, false);



//        MusicUtils.getMusicInfo(this,false,);
        for (int i = 0; i < arrayList.size(); i++) {
            MusicInfo info = arrayList.get(i);

            String musicsiner = info.getSinger()  ;


            int musiccount = info.getSingermusicCount();

            Bitmap bitmap = info.getMusicAlbumsImage();

            ContentItem item;
            if (bitmap != null) {
                item = new ContentItem(bitmap, R.drawable.more_title_selected, musicsiner, musiccount + "首");
            } else {
                item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, musicsiner, musiccount + "首");
            }
            items.add(item);

            //绑定服务
            //MusicUtils.bindToService(this);

        }

        }








    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }




    @Override
    public void onConvertViewClicked(int position) {

        /*MusicInfo info = arrayList.get(position);
        cursor = MusicUtils.getMusicInfo(this, false, MediaStore.Audio.Media._ID + "=?"
                , new String[]{String.valueOf(info.getMusicId())});

        if (cursor.getCount() == 0) {
            return;
        }
        if (cursor instanceof TrackBrowserActivity.NowPlayingCursor) {
            if (MusicUtils.sService != null) {
                try {
                    MusicUtils.sService.setQueuePosition(position);
                    return;
                } catch (RemoteException ex) {
                }
            }
        }
        //播放音乐
        MusicUtils.playAll(this, cursor);*/


        super.playCursor(arrayList, false, position);


    }



}


