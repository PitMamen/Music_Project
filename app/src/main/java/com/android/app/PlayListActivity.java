package com.android.app;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.SongList;
import com.dlighttech.music.util.DialogUtils;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class PlayListActivity extends BaseActivity
        implements ContentAdapter.OnConvertViewClicked
        , Observer {

    private ListView mListView;
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ContentAdapter mAdapter;
    private ArrayList<SongList> songLists;
    private SideBar sideBar;


    @Override
    public void onCreateView() {
        super.setTitleText("播放列表");
        mListView = (ListView) findViewById(R.id.lv_music_detail);

        sideBar = (SideBar) findViewById(R.id.navigation_bar);

        DataChangedWatcher.getInstance().registerObserver(this);

        mAdapter = new ContentAdapter(this, mItems, false);
        mAdapter.setIsOperationHidden(true);

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

        ivThumb.setImageResource(R.drawable.create_playlist);
        ivOpera.setImageResource(R.drawable.arrow_list);
        ivOpera.setVisibility(View.GONE);
        tvName.setText("新建歌单");

        mListView.addFooterView(footView);
        mListView.setAdapter(mAdapter);


        sideBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int position, String selectStr) {

                //  Toast.makeText(PlayListActivity.this,"按键导航操作...",Toast.LENGTH_SHORT).show();


            }
        });

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
                // 判断当前歌单名称是否存在
                boolean isExists = DataBaseManager.getInstance(PlayListActivity.this)
                        .isExistsName(songListName);
                if (isExists) {
                    Toast.makeText(PlayListActivity.this
                            , "歌单名称已经存在，请重新命名！", Toast.LENGTH_SHORT).show();
                    etSong.setText(null);
                    return;
                }

                // 添加一条数据到歌单表
                SongList list = new SongList();
                list.setName(songListName);
                list.setCount(0);
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
        mItems.clear();
        songLists.clear();
        songLists = DataBaseManager.getInstance(this).getAllSongList();
        for (int i = 0; i < songLists.size(); i++) {
            int resId = i == 0 ? R.drawable.favorites_playlist : R.drawable.champions_playlist;
            ContentItem item = new ContentItem(resId
                    , songLists.get(i).getName()
                    , songLists.get(i).getCount() + "首");
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
        songLists = DataBaseManager.getInstance(this).getAllSongList();

        if (songLists == null || songLists.size() == 0) {
            SongList list = new SongList();
            list.setName("我喜欢听");
            list.setCount(0);
            DataBaseManager.getInstance(this).insertSongList(list);
            ContentItem item = new ContentItem(R.drawable.favorites_playlist
                    , "我喜欢听", "0首");
            mItems.add(item);
        } else {
            for (int i = 0; i < songLists.size(); i++) {
                int resId = i == 0 ? R.drawable.favorites_playlist : R.drawable.champions_playlist;
                ContentItem item = new ContentItem(resId
                        , songLists.get(i).getName()
                        , songLists.get(i).getCount() + "首");
                mItems.add(item);
                Log.d("TAG", item.toString());
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
        super.removeAllMsg();
        // 当点击一个item时获取当前歌单的id，根据id获取该歌单下的所有歌曲
        ContentItem item = mItems.get(position);
        SongList list = songLists.get(position);
        if (list == null || list.getCount() == 0) {
            Toast.makeText(PlayListActivity.this, "\"" + item.getTitle() + "\"下还没有收录歌曲！"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        // 保存点击哪一个歌单时的id号
        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.SONG_LIST_ID_KEY, list.getId());

        // 保存是否为歌单
        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, true);

        Intent intent = new Intent(PlayListActivity.this, SongOfSongListActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("songListName",list.getName());
        intent.putExtra("id", list.getId());
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object data) {
//        super.update(observable,data);
        // 通知观察者更新 song count
        if (data instanceof SongList) {
            SongList list = (SongList) data;
            DataBaseManager.getInstance(this)
                    .updateCountBySongList(list);
            updateAdapter();
            Log.d("TAG", "song list count update success! count ===" + list.getCount());
        }
    }
}

