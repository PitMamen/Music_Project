package com.android.app;

import android.database.Cursor;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;

public class MusicListActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {

    /**
     * implements ContentAdapter.OnOperateClicked
     * , AdapterView.OnItemClickListener
     */

    private ArrayList<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ContentItem item;
    private Cursor mTrackCursor;
    private MusicUtils.ServiceToken mToken;
    //    private ListPopupWindow popupWindow;

    @Override
    public void onCreateView() {
        super.setTitleText(item != null ? item.getTitle() : "N/A"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_sing_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(mMusicList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_music_list);
    }

    @Override
    public void onCreateData() {
        // 当前操作表示音乐文件将被删除
        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, false);

        item = (ContentItem) getIntent().getSerializableExtra("item");
        ArrayList<MusicInfo> musicInfos = getIntent().getParcelableArrayListExtra("musicInfos");

        for (int i = 0; i < musicInfos.size(); i++) {
            MusicInfo info = musicInfos.get(i);
            File musicFile = new File(info.getMusicPath());
            if (musicFile.exists()) {
                String parent = musicFile.getParent();
                if (parent.equals(item.getContent())) {
                    mMusicList.add(info);
                }
            }
        }

        // 设置音乐数据
        for (int i = 0; i < mMusicList.size(); i++) {
            MusicInfo info = mMusicList.get(i);

            ContentItem newItem = new ContentItem(info.getMusicAlbumsImage()
                    , R.drawable.more_title_selected
                    , info.getMusicName()
                    , info.getSinger());
            mItems.add(newItem);
        }

        // 绑定服务
        mToken = MusicUtils.bindToService(this);

    }


    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }


    @Override
    public void onConvertViewClicked(int position) {
        MusicInfo info = mMusicList.get(position);
        mTrackCursor = MusicUtils.getMusicInfo(this, false, MediaStore.Audio.Media._ID + "=?"
                , new String[]{String.valueOf(info.getMusicId())});

        if (mTrackCursor.getCount() == 0) {
            return;
        }
        if (mTrackCursor instanceof TrackBrowserActivity.NowPlayingCursor) {
            if (MusicUtils.sService != null) {
                try {
                    MusicUtils.sService.setQueuePosition(position);
                    return;
                } catch (RemoteException ex) {
                }
            }
        }
        MusicUtils.playAll(this, mTrackCursor);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicUtils.unbindFromService(mToken);
    }
}
