package com.android.app;

import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MusicListActivity extends BaseActivity {

    private ArrayList<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ContentItem item;


    @Override
    public void onCreateView() {
        super.setTitleText(item != null ? item.getTitle() : "N/A"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_sing_list);
        mAdapter = new ContentAdapter(this, mItems);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_music_list);
    }

    @Override
    public void onCreateData() {
        item = (ContentItem) getIntent().getSerializableExtra("item");
        ArrayList<MusicInfo> musicInfos = getIntent().getParcelableArrayListExtra("musicInfos");

        for (int i = 0; i < musicInfos.size(); i++) {
            MusicInfo info = musicInfos.get(i);
            String parent = FileUtils.getFileParent(new File(info.getMusicPath()));
            if (parent.equals(item.getContent())) {
                mMusicList.add(info);
            }
        }

        // 设置音乐数据
        for (int i = 0; i < mMusicList.size(); i++) {
            MusicInfo info = mMusicList.get(i);

            ContentItem newItem = new ContentItem(R.drawable.app_music
                    , R.drawable.ic_menu_eq
                    , info.getMusicName()
                    , info.getSinger());
            mItems.add(newItem);
        }
    }


    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
