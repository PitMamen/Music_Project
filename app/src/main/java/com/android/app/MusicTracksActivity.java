package com.android.app;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/6/23.
 */
public class MusicTracksActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ImageButton ib_music_back;
    private TextView tv_title, tv_paly_mode, tv_music_number;
    private ImageView iv_music_search, iv_music_icon;
    private ListView lv_music_detail;
    private SideBar sb_navigation_bar;
    private Cursor cursor;
    private  MusicUtils.ServiceToken token;

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
        super.setTitleText("Music");

        iv_music_icon = (ImageView) findViewById(R.id.iv_mode_icon);

        tv_paly_mode = (TextView) findViewById(R.id.tv_play_mode);

        tv_music_number = (TextView) findViewById(R.id.tv_music_num);


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        lv_music_detail = (ListView) findViewById(R.id.lv_music_detail);
        mAdapter = new ContentAdapter(this, mItems, true);

        mAdapter.setMusicInfos(arrayList);

        lv_music_detail.setAdapter(mAdapter);


        int count = mAdapter.getCount();

        tv_music_number.setText("共" + count + "首");


        sb_navigation_bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int position, String selectStr) {


            }
        });


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

    }

    @Override
    public void onSearchSubmit(String text) {

    }

    @Override
    public void onConvertViewClicked(int position) {
        MusicInfo info = arrayList.get(position);
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
        MusicUtils.playAll(this, cursor);

    }

}
