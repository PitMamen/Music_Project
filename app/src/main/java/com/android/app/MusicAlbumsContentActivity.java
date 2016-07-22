package com.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.handler.LoadingDataTask;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxinkai001 on 2016/7/21.
 */
public class MusicAlbumsContentActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {


    private ListView mlistview;

    private ContentAdapter adapter;
    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private List<MusicInfo> infos;
    private ProgressBar mProgressBar;
    private long mAlbumsId = 0;


    private LoadingDataTask.OnLoadDataListener<MusicInfo> mListener =
            new LoadingDataTask.OnLoadDataListener<MusicInfo>() {

                @Override
                public List<MusicInfo> onLoadingData() {
                    return MusicUtils.getMusicInfo(MusicAlbumsContentActivity.this
                            , MediaStore.Audio.Media.ALBUM_ID + " =?"
                            , new String[]{String.valueOf(mAlbumsId)}, false);
                }

                @Override
                public void onLoadDataSuccess(List<MusicInfo> mParams) {
                    mProgressBar.setVisibility(View.GONE);
                    if (mParams == null || mParams.size() == 0) {
                        return;
                    }
                    infos = mParams;
                    //根据专辑Id查询歌曲信息
                    for (int i = 0; i < infos.size(); i++) {
                        MusicInfo info = infos.get(i);
                        Log.d("bibi", "专辑名==" + info.getMusicAlbumsName() + "专辑下的歌曲+++" + info.getMusicName());
                        Bitmap bitmap = MusicUtils.getArtwork(MusicAlbumsContentActivity.this
                                , info.getMusicId(), info.getAlbumId());

                        String MusicName = info.getMusicName();
                        String singer = info.getSinger();

                        ContentItem item = new ContentItem(bitmap, R.drawable.more_title_selected, MusicName, singer);
                        items.add(item);
                    }

                    adapter = new ContentAdapter(MusicAlbumsContentActivity.this, items, true);
                    mlistview.setAdapter(adapter);
                    MusicAlbumsContentActivity.this.setVisiblePlayMode(true);
                    MusicAlbumsContentActivity.this.setSongCount(adapter.getCount());

                }

                @Override
                public void onLoadDataFail(String msg) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MusicAlbumsContentActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            };

    @Override
    public void onInitView() {
        setContentView(R.layout.music_albums_detail);
    }

    @Override
    public void onCreateView() {
        super.setTitleText("专辑详情");
        mlistview = (ListView) findViewById(R.id.lv_music);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreateData() {
        Intent intent = getIntent();
        mAlbumsId = intent.getLongExtra("AlbumsId", 0L);
        new LoadingDataTask<MusicInfo>(mListener).doInBackGround();
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }


    @Override
    public void onConvertViewClicked(int position) {

        super.playCursor(infos, false, position);


    }
}
