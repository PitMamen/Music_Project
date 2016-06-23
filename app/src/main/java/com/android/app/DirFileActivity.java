package com.android.app;

import android.widget.ListView;

import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class DirFileActivity extends BaseActivity {

    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;

    @Override
    public void onCreateView() {


        super.setTitleText("文件夹"); // 设置title


    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_dir_file);
    }

    @Override
    public void onCreateData() {
        ArrayList<MusicInfo> musicInfos = getIntent().getParcelableArrayListExtra("musicInfos");
        for (MusicInfo info : musicInfos) {
            // 获取文件的目录集合
            File singFile = new File(info.getMusicPath());
            String dirPath = FileUtils.getFileParent(singFile);
            ContentItem item = new ContentItem(R.drawable.app_music
                    , R.drawable.ic_menu_shuffle
                    , new File(dirPath).getName()
                    , dirPath);
            mItems.add(item);
        }
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}
