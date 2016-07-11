package com.android.app;

import android.graphics.Bitmap;
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
        getWindow().setBackgroundDrawable(getDrawable(android.R.color.white));
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
                MusicInfo info = infos.get(i);
                String name = info.getMusicName();
                String singer = info.getSinger();
                Bitmap bitmap = info.getMusicAlbumsImage();
                ContentItem item;
                if (bitmap != null) {
                    item = new ContentItem(bitmap, R.drawable.more_title_selected, name, singer);
                } else {
                    item = new ContentItem(R.drawable.albums_list, R.drawable.more_title_selected, name, singer);
                }

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
