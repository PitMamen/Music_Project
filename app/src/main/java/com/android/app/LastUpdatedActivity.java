package com.android.app;

import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class LastUpdatedActivity extends BaseActivity {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private List<ContentItem> mItems = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();

    @Override
    public void onCreateView() {
        super.setTitleText("最近更新");
        mListView = (ListView) findViewById(R.id.lv_updated_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(infos);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_last_updated);
    }

    @Override
    public void onCreateData() {

        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, false);

        infos = MusicUtils.getMusicInfo(this, true);
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                ContentItem item = new ContentItem(R.drawable.app_music
                        , R.drawable.ic_menu_eq
                        , infos.get(i).getMusicName()
                        , infos.get(i).getSinger());

                mItems.add(item);
            }
        }
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
