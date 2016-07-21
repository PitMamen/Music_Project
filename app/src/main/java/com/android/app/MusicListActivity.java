package com.android.app;

import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

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

    @Override
    public void onCreateView() {
        super.setTitleText(item != null ? item.getTitle() : "N/A"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_sing_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(mMusicList);
        mListView.setAdapter(mAdapter);
        super.setVisiblePlayMode(true);
        super.setSongCount(mAdapter.getCount());
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
            String newPath = info.getMusicPath().substring(0, info.getMusicPath().lastIndexOf("/"));
            if (newPath.equals(item.getContent())) {
                ContentItem newItem = new ContentItem(info.getMusicAlbumsImage()
                        , R.drawable.more_title_selected
                        , info.getMusicName()
                        , info.getSinger());
                mItems.add(newItem);
                mMusicList.add(info);
            }

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
        // 当点击了listView item将在底部布局中播放音乐
        super.playCursor(mMusicList, false, position);
    }
}
