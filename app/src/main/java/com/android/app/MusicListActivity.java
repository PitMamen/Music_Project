package com.android.app;

import android.provider.MediaStore;
import android.util.Log;
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
        playMusic(position);
    }

    private void playMusic(int position) {
        StringBuilder mSelection = new StringBuilder();
        String[] mSelectionArgs = new String[mMusicList.size()];
        // 由于需要下一曲的播放所以需要将当前目录下的歌曲以游标的形式传递给Service
        for (int i = 0; i < mMusicList.size(); i++) {

            mSelection.append(MediaStore.Audio.Media._ID + "=?");
            mSelection.append(i == mMusicList.size() - 1 ? "" : " or ");

            mSelectionArgs[i] = String.valueOf(mMusicList.get(i).getMusicId());
        }
        super.playCursor(mSelection.toString(), mSelectionArgs, false, position);
        Log.d("TAG", mSelection.toString());
    }
}
