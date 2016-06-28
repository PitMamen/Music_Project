package com.android.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.Song;
import com.dlighttech.music.util.DialogUtils;

import java.util.ArrayList;

public class SongOfSongListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener
        , ContentAdapter.OnOperateClicked {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private ArrayList<Song> mSongs = new ArrayList<Song>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListPopupWindow popupWindow;
    private int mPosition;


    @Override
    public void onCreateView() {
        mListView = (ListView) findViewById(R.id.lv_song_of_list);
        mAdapter = new ContentAdapter(this, mItems);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_song_of_song_list);
    }

    @Override
    public void onCreateData() {
        int id = getIntent().getIntExtra("id", 0);
        mSongs = DataBaseManager.getInstance(this).getSongByListId(id);
        if (mSongs != null && mSongs.size() > 0) {
            for (int i = 0; i < mSongs.size(); i++) {
                Song song = mSongs.get(i);
                ContentItem item = new ContentItem(R.drawable.app_music
                        , R.drawable.ic_menu_eq
                        , song.getName()
                        , song.getSinger());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    /**
     * 创建一个popupWindow菜单
     *
     * @param v
     */
    private void createPopupWindowMenu(View v) {
        String[] menus = {"新建歌单", "添加到歌单", "查看歌曲信息", "删除"};
        popupWindow = DialogUtils.createListPopupWindow(this, menus, v, this);
        popupWindow.show();
    }

    @Override
    public void onOperateClicked(int position, View v) {
        mPosition = position;
        createPopupWindowMenu(v);
    }
}
