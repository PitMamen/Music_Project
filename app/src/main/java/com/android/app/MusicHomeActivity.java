package com.android.app;

import android.util.Log;
import android.widget.ListView;

import com.dlighttech.music.model.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class MusicHomeActivity extends BaseActivity {

    private ListView mListView;
    private List<ContentItem> mData;

    @Override
    public void onCreateView() {
        setContentView(R.layout.music_home_page_layout);
        mListView = (ListView) findViewById(R.id.home_list_view);


    }

    @Override
    public void onCreateData() {
        mData = new ArrayList<ContentItem>();
//        ContentItem itemDir = new ContentItem()

    }

    @Override
    public void onSearchTextChanged(String text) {
        Log.d("TAG", text);
    }

    @Override
    public void onSearchSubmit(String text) {
        Log.d("TAG", text);
    }


}
