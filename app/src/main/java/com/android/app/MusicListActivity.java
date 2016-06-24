package com.android.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.CommonUtils;
import com.dlighttech.music.util.DialogUtils;
import com.dlighttech.music.util.DisplayUtils;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MusicListActivity extends BaseActivity
        implements ContentAdapter.OnOperateClicked
        , AdapterView.OnItemClickListener {

    private ArrayList<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ContentItem item;
    private ListPopupWindow popupWindow;
    private int mSelectionPos = 0;

    @Override
    public void onCreateView() {
        super.setTitleText(item != null ? item.getTitle() : "N/A"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_sing_list);
        mAdapter = new ContentAdapter(this, mItems);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_music_list);
    }

    @Override
    public void onCreateData() {
        item = (ContentItem) getIntent().getSerializableExtra("item");
        ArrayList<MusicInfo> musicInfos = getIntent().getParcelableArrayListExtra("musicInfos");

        for (int i = 0; i < musicInfos.size(); i++) {
            MusicInfo info = musicInfos.get(i);
            String parent = FileUtils.getFileParent(new File(info.getMusicPath()));
            if (parent.equals(item.getContent())) {
                mMusicList.add(info);
            }
        }

        // 设置音乐数据
        for (int i = 0; i < mMusicList.size(); i++) {
            MusicInfo info = mMusicList.get(i);

            ContentItem newItem = new ContentItem(R.drawable.app_music
                    , R.drawable.ic_menu_eq
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
    public void onOperateClicked(int position, View v) {
        mSelectionPos = position;
        // 当点击右侧菜单时弹出popupWindow
        createPopupWindowMenu(v);
    }


    private MusicInfo getCurrMusic() {
        return mMusicList.get(mSelectionPos);
    }

    /**
     * 创建一个popupWindow菜单
     *
     * @param v
     */
    private void createPopupWindowMenu(View v) {
        popupWindow = new ListPopupWindow(this);
        popupWindow.setWidth(DisplayUtils.dip2px(this, 200));
        popupWindow.setHeight(DisplayUtils.dip2px(this, 200));
        String[] menus = {"新建歌单", "添加到歌单", "查看歌曲信息", "删除"};
        popupWindow.setAdapter(new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1, menus));
        popupWindow.setAnchorView(v);
        popupWindow.setModal(true);
        popupWindow.setOnItemClickListener(this);
        popupWindow.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();

        if (position == 0 || position == 1) {
            // 新建歌单, 添加到歌单
            Intent intent = new Intent(MusicListActivity.this, PlayListActivity.class);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (position == 2) {
            // 创建歌曲详细信息显示dialog
            createSongDetailDialog();
        } else if (position == 3) {
            // 删除
            deleteMusic();
        }
    }

    /**
     * 删除音乐
     */
    private void deleteMusic() {
        MusicInfo info = getCurrMusic();
        String path = info.getMusicPath();
        File musicFile = new File(path);
        if (!musicFile.exists()) {
            return;
        }
        musicFile.delete();
        mItems.remove(mSelectionPos);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 创建歌曲详细信息的dialog
     */
    private void createSongDetailDialog() {
        if (mMusicList == null || mMusicList.size() == 0) {
            return;
        }
        View songDetailView = getLayoutInflater().inflate(R.layout.dialog_song_detail, null);
        TextView tvSongName = (TextView) songDetailView.findViewById(R.id.tv_song_name);
        TextView tvSongArtist = (TextView) songDetailView.findViewById(R.id.tv_song_artist);
        TextView tvSongTime = (TextView) songDetailView.findViewById(R.id.tv_song_time);
        TextView tvSongPath = (TextView) songDetailView.findViewById(R.id.tv_song_path);
        TextView tvSongAlbum = (TextView) songDetailView.findViewById(R.id.tv_song_album);
        TextView tvSongSize = (TextView) songDetailView.findViewById(R.id.tv_song_size);
        Button btnOK = (Button) songDetailView.findViewById(R.id.btn_ok);

        MusicInfo info = getCurrMusic();
        tvSongName.setText(info.getMusicName());
        tvSongArtist.setText(info.getSinger());
        tvSongTime.setText(CommonUtils.stringForTime(info.getTotalTime()));
        tvSongPath.setText(info.getMusicPath());
        tvSongAlbum.setText(info.getMusicAlbumsName().equals("0")
                ? "unknown" : info.getMusicAlbumsName());
        tvSongSize.setText(Formatter.formatFileSize(this, info.getMusicSize()));

        final AlertDialog dialog = DialogUtils
                .createContentDialog(MusicListActivity.this, songDetailView);

        dialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}
