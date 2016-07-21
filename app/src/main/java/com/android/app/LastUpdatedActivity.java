package com.android.app;

import android.graphics.Bitmap;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class LastUpdatedActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked {

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
        super.setVisiblePlayMode(true); // 是否显示play mode 状态栏
        super.setSongCount(mAdapter.getCount()); // 设置状态上的数字
    }


    @Override
    public void onInitView() {
        setContentView(R.layout.activity_last_updated);
    }

    @Override
    public void onCreateData() {
//        DataChangedWatcher.getInstance().registerObserver(this);

        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, false);

        ArrayList<MusicInfo> infos = MusicUtils.getMusicInfo(this, true);
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                MusicInfo info = infos.get(i);
                String name = info.getMusicName();
                String singer = info.getSinger();
                Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId());
                ContentItem item;
                item = new ContentItem(bitmap, R.drawable.more_title_selected, name, singer);
                mItems.add(item);
                this.infos.add(info);
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
        super.playCursor(infos, true, position);
    }


}
