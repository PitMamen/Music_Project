package com.android.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlighttech.music.adapter.HomeAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class MusicHomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<ContentItem> mData;
    private HomeAdapter mAdapter;
    private TextView tvTracks, tvAlbums, tvArtist;
    private ArrayList<MusicInfo> mMusicInfos;
    private static final int MUSIC_LOADING = 0;
    private static final int MUSIC_LOAD_COMPLETE = 1;
    private static final int MUSIC_LOAD_FAIL = 2;
    private boolean isRefresh;

    /**
     * 中间布局的三个textview点击事件
     */
    private View.OnClickListener middleLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_tracks: // 曲目
               Intent intenttracks = new Intent(MusicHomeActivity.this,MusicTracksActivity.class);
                    startActivity(intenttracks);
                    break;
                case R.id.tv_album: // 专辑
                    Intent intentalbums  =new Intent(MusicHomeActivity.this,MusicAlbumsActivity.class);
                     startActivity(intentalbums);
                    break;
                case R.id.tv_artist: // 歌手
                    Intent intentsinger = new Intent(MusicHomeActivity.this,MusicSingerActivity.class);
                    startActivity(intentsinger);
                    break;
            }
        }
    };

    /**
     * 音乐资源加载
     */
    private Runnable mMusicLoadTask = new Runnable() {
        @Override
        public void run() {
            MusicUtils.getMusicInfo(MusicHomeActivity.this
                    , new MusicUtils.OnMusicLoadedListener() {
                        @Override
                        public void onMusicLoadSuccess(ArrayList<MusicInfo> infos) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message.obtain(mHandler, MUSIC_LOAD_COMPLETE, infos).sendToTarget();
                        }

                        @Override
                        public void onMusicLoading() {
                            Message.obtain(mHandler, MUSIC_LOADING).sendToTarget();
                        }

                        @Override
                        public void onMusicLoadFail() {
                            Message.obtain(mHandler, MUSIC_LOAD_FAIL).sendToTarget();
                        }
                    });
        }
    };

    private Handler mHandler = new Handler(Looper.getMainLooper()) {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MUSIC_LOAD_COMPLETE:
                    mMusicInfos = (ArrayList<MusicInfo>) msg.obj;
                    mAdapter.updateItem(); // 更新itemView
                    isRefresh = true;
                    break;
                case MUSIC_LOADING:
                    isRefresh = false;
                    break;
                case MUSIC_LOAD_FAIL:
                    Toast.makeText(MusicHomeActivity.this
                            , "加载异常", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                    break;
            }
        }
    };

    @Override
    public void onInitView() {
        setContentView(R.layout.music_home_page_layout);
    }

    @Override
    public void onCreateView() {
        tvTracks = (TextView) findViewById(R.id.tv_tracks);
        tvAlbums = (TextView) findViewById(R.id.tv_album);
        tvArtist = (TextView) findViewById(R.id.tv_artist);

        tvTracks.setOnClickListener(middleLayoutListener);
        tvAlbums.setOnClickListener(middleLayoutListener);
        tvArtist.setOnClickListener(middleLayoutListener);

        mListView = (ListView) findViewById(R.id.home_list_view);
        mAdapter = new HomeAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        // 开启一个线程用于加载当前手机所有文件夹下的音乐资源
        new Thread(mMusicLoadTask).start();
    }

    @Override
    public void onCreateData() {
        mData = new ArrayList<ContentItem>();
        ContentItem itemDir = new ContentItem(R.drawable.seek_thumb
                , R.drawable.left, "文件夹");
        ContentItem itemPlayList = new ContentItem(R.drawable.playlist_tile
                , R.drawable.left, "播放列表");
        ContentItem itemUpdate = new ContentItem(R.drawable.app_music
                , R.drawable.left, "最近更新");
        ContentItem itemPlay = new ContentItem(R.drawable.stat_notify_musicplayer
                , R.drawable.left, "最近播放");
        mData.add(itemDir);
        mData.add(itemPlayList);
        mData.add(itemUpdate);
        mData.add(itemPlay);
    }

    @Override
    public void onSearchTextChanged(String text) {
        Log.d("TAG", text);
    }

    @Override
    public void onSearchSubmit(String text) {
        Log.d("TAG", text);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // 文件夹
            // 判断当前是否正在刷新中
            if (!isRefresh) {
                Toast.makeText(MusicHomeActivity.this
                        , "正在刷新中请稍候！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mMusicInfos != null && mMusicInfos.size() > 0) {
                Intent intent = new Intent(MusicHomeActivity.this
                        , DirFileActivity.class);
                intent.putParcelableArrayListExtra("musicInfos", mMusicInfos);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(MusicHomeActivity.this
                        , "手机中没有音乐文件!", Toast.LENGTH_SHORT).show();
            }
        } else if (position == 1) {
            // 播放列表
            Intent intent = new Intent(MusicHomeActivity.this, PlayListActivity.class);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
