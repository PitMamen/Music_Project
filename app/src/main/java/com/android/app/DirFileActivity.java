package com.android.app;

import android.content.Intent;
import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class DirFileActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked,Observer {

    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    /**
     * 当前音乐文件的父目录
     */
    private ArrayList<String> mDirs = new ArrayList<String>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ArrayList<MusicInfo> mMusicInfos;

    @Override
    public void onCreateView() {
        super.setTitleText("文件夹"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_music_dir);
        mAdapter = new ContentAdapter(DirFileActivity.this, mItems,false);
        mAdapter.setIsOperationHidden(true); // 是否隐藏右侧图标
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_dir_file);
    }

    @Override
    public void onCreateData() {
        mMusicInfos = getIntent().getParcelableArrayListExtra("musicInfos");
        for (int i = 0; i < mMusicInfos.size(); i++) {

            MusicInfo info = mMusicInfos.get(i);
            // 获取文件的目录集合
            File singFile = new File(info.getMusicPath());
            if (singFile.exists()) {
                String dirPath = FileUtils.getFileParent(singFile);
                if (!mDirs.contains(dirPath)) {
                    ContentItem item = new ContentItem(R.drawable.folder_list
                            , R.drawable.more_title
                            , new File(dirPath).getName()
                            , dirPath);
                    mItems.add(item);
                }
                mDirs.add(dirPath);
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
        ContentItem item = mItems.get(position);
        Intent intent = new Intent(DirFileActivity.this, MusicListActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", item);
        intent.putParcelableArrayListExtra("musicInfos", mMusicInfos);
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
