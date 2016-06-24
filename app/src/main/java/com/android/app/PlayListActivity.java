package com.android.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;

import java.util.ArrayList;

public class PlayListActivity extends BaseActivity {

    private ListView mListView;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;

    @Override
    public void onCreateView() {
        super.setTitleText("播放列表");
        mListView = (ListView) findViewById(R.id.lv_play_list);
        mAdapter = new ContentAdapter(this, mItems);

        View footView = getLayoutInflater().inflate(R.layout.home_list_item, null);
        ImageView ivThumb = (ImageView) footView.findViewById(R.id.thumb_imageView_content);
        ImageView ivOpera = (ImageView) footView.findViewById(R.id.operate_imageView_content);
        TextView tvName = (TextView) footView.findViewById(R.id.title_content);

        ivThumb.setImageResource(R.drawable.ic_mp_album_playback);
        ivOpera.setImageResource(R.drawable.left);
        tvName.setText("新建歌单");

        mListView.addFooterView(footView);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    public void onCreateData() {
        ContentItem item = new ContentItem(R.drawable.app_music, R.drawable.left, "我喜欢听", "0首");
        mItems.add(item);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }
}

