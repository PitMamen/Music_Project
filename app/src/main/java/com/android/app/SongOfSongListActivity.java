package com.android.app;

import android.provider.MediaStore;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;

public class SongOfSongListActivity extends BaseActivity {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private ArrayList<Song> mSongs = new ArrayList<Song>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> mMusicInfos = new ArrayList<MusicInfo>();
//    private int mPosition;


    @Override
    public void onCreateView() {
        mListView = (ListView) findViewById(R.id.lv_song_of_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(mMusicInfos);
        mListView.setAdapter(mAdapter);
    }

//    private MusicInfo getCurrMusicInfo() {
//        // 根据歌曲名称获取歌曲信息
//        String songName = mSongs.get(mPosition).getName();
//        String selection = MediaStore.Audio.Media.TITLE + "=?";
//        String[] selectArgs = {songName};
//        return MusicUtils.getMusicInfoByArgs(this, selection, selectArgs);
//    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_song_of_song_list);
    }

    @Override
    public void onCreateData() {
        // 当前popupWindow的删除操作为只将音乐从歌单中删除
        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, true);

        int id = getIntent().getIntExtra("id", 0);
        mSongs = DataBaseManager.getInstance(this).getSongByListId(id);
        if (mSongs != null && mSongs.size() > 0) {
            for (int i = 0; i < mSongs.size(); i++) {
                Song song = mSongs.get(i);
                ContentItem item = new ContentItem(R.drawable.singer
                        , R.drawable.more_title_selected
                        , song.getName()
                        , song.getSinger());

                MusicInfo info = MusicUtils.getMusicInfoByArgs(this, false
                        , MediaStore.Audio.Media.DATA + "=?", new String[]{song.getSongPath()});
                mMusicInfos.add(info);
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


//    /**
//     * 创建一个popupWindow菜单
//     *
//     * @param v
//     */
//    private void createPopupWindowMenu(View v) {
//        String[] menus = {"新建歌单", "添加到歌单", "查看歌曲信息", "删除"};
//        popupWindow = DialogUtils.createListPopupWindow(this, menus, v, this);
//        popupWindow.show();
//    }

//    @Override
//    public void onOperateClicked(int position, View v) {
//        mPosition = position;
//    }
}
