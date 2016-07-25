package com.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by pengxinkai001 on 2016/7/21.
 */
public class MusicAlbumsContentActivity extends  BaseActivity implements ContentAdapter.OnConvertViewClicked{


    private ListView mlistview;

    private ContentAdapter adapter;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private  ArrayList<MusicInfo> infos;



    @Override
    public void onInitView() {

      setContentView(R.layout.music_albums_detail);


    }
    @Override
    public void onCreateView() {

        super.setTitleText("专辑详情");

        mlistview = (ListView) findViewById(R.id.lv_albums_datail);

        adapter = new ContentAdapter(this,items,true);

        adapter.setMusicInfos(infos);

        super.setVisiblePlayMode(true);
        super.setSongCount(adapter.getCount());
        mlistview.setAdapter(adapter);
    }


    @Override
    public void onCreateData() {

        Intent intent = getIntent();
        long albumsId = intent.getLongExtra("AlbumsId",0L);

        //根据专辑Id查询歌曲信息
        infos = MusicUtils.getMusicInfo(this, MediaStore.Audio.Media.ALBUM_ID+" =?",new String[]{String.valueOf(albumsId)},false);

        for (int i = 0; i <infos.size() ; i++) {

            MusicInfo info = infos.get(i);

            Log.d("bibi", "专辑名=="+info.getMusicAlbumsName()+"专辑下的歌曲+++"+info.getMusicName());


            Bitmap bitmap = MusicUtils.getArtwork(this,info.getMusicId(),info.getAlbumId());

            String MusicName = info.getMusicName();
            String singer = info.getSinger();

            ContentItem item = new ContentItem(bitmap,R.drawable.more_title_selected,MusicName,singer);

            items.add(item);



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

        super.playCursor(infos,false,position);



    }
}
