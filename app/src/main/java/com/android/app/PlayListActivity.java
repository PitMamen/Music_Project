package com.android.app;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.SongList;
import com.dlighttech.music.util.DialogUtils;

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

        View footView = getLayoutInflater().inflate(R.layout.content_layout, null);
        ImageView ivThumb = (ImageView) footView.findViewById(R.id.thumb_imageView_content);
        ImageView ivOpera = (ImageView) footView.findViewById(R.id.operate_imageView_content);
        TextView tvName = (TextView) footView.findViewById(R.id.title_content);
        TextView tvContent = (TextView) footView.findViewById(R.id.content_content);
        tvContent.setVisibility(View.GONE);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSongListDialog();
            }
        });

        ivThumb.setImageResource(R.drawable.ic_mp_album_playback);
        ivOpera.setImageResource(R.drawable.left);
        tvName.setText("新建歌单");

        mListView.addFooterView(footView);
        mListView.setAdapter(mAdapter);

    }

    /**
     * 创建歌单dialog
     */
    private void createSongListDialog() {
        View songListView = getLayoutInflater().inflate(R.layout.dialog_song_list, null);
        final EditText etSong = (EditText) songListView.findViewById(R.id.et_song_list);
        Button btnOk = (Button) songListView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) songListView.findViewById(R.id.btn_cancel);

        final AlertDialog dialog = DialogUtils.createContentDialog(PlayListActivity.this, songListView);
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songListName = etSong.getText().toString();
                if (TextUtils.isEmpty(songListName)) {
                    Toast.makeText(PlayListActivity.this
                            , "歌单名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 添加一条数据到歌单表
                SongList list = new SongList();
                list.setName(songListName);
                list.setCount("0首");
                boolean isInsert = DataBaseManager.getInstance(PlayListActivity.this)
                        .insertSongList(list);
                if (isInsert) {
                    Log.d("TAG", "歌单添加OK");
                    dialog.dismiss();
                    // 如果添加ok刷新adapter
                    updateAdapter();
                } else {
                    Log.d("TAG", "歌单添加FAIL");
                }
            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 刷新数据
    private void updateAdapter() {
        ArrayList<SongList> newList = DataBaseManager.getInstance(this).getAllSongList();
        mItems.clear();
        for (int i = 0; i < newList.size(); i++) {
            ContentItem item = new ContentItem(R.drawable.app_music
                    , R.drawable.left
                    , newList.get(i).getName()
                    , newList.get(i).getCount());
            mItems.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onInitView() {
        setContentView(R.layout.activity_play_list);
    }

    @Override
    public void onCreateData() {
        mItems.clear();
        // 首先获取全部歌单，如果表中没有数据，那么添加“我喜欢听”，否则全部查询
        ArrayList<SongList> songLists = DataBaseManager.getInstance(this).getAllSongList();
        if (songLists == null || songLists.size() == 0) {
            SongList list = new SongList();
            list.setName("我喜欢听");
            list.setCount("0首");
            DataBaseManager.getInstance(this).insertSongList(list);
            ContentItem item = new ContentItem(R.drawable.app_music
                    , R.drawable.left, "我喜欢听", "0首");
            mItems.add(item);
        } else {
            for (int i = 0; i < songLists.size(); i++) {
                ContentItem item = new ContentItem(R.drawable.app_music
                        , R.drawable.left, songLists.get(i).getName()
                        , songLists.get(i).getCount());
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

