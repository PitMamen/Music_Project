package com.android.app;

import android.widget.ListView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MusicListActivity extends BaseActivity {

    /**
     * implements ContentAdapter.OnOperateClicked
     * , AdapterView.OnItemClickListener
     */

    private ArrayList<MusicInfo> mMusicList = new ArrayList<MusicInfo>();
    private ArrayList<ContentItem> mItems = new ArrayList<ContentItem>();
    private ListView mListView;
    private ContentAdapter mAdapter;
    private ContentItem item;
//    private ListPopupWindow popupWindow;
    private int mSelectionPos = 0;

    @Override
    public void onCreateView() {
        super.setTitleText(item != null ? item.getTitle() : "N/A"); // 设置title
        mListView = (ListView) findViewById(R.id.lv_sing_list);
        mAdapter = new ContentAdapter(this, mItems);
        mAdapter.setMenu(true);
        mAdapter.setMusicInfo(getCurrMusic());
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
            File musicFile = new File(info.getMusicPath());
            if (musicFile.exists()) {
                String parent = FileUtils.getFileParent(musicFile);
                if (parent.trim().equals(item.getContent().trim())) {
                    mMusicList.add(info);
                }
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

//    @Override
//    public void onOperateClicked(int position, View v) {
//        mSelectionPos = position;
//        // 当点击右侧菜单时弹出popupWindow
//        createPopupWindowMenu(v);
//    }


    private MusicInfo getCurrMusic() {
        return mMusicList.get(mSelectionPos);
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
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        popupWindow.dismiss();
//        if (position == 0) {
//            // 新建歌单, 添加到歌单
//            Intent intent = new Intent(MusicListActivity.this, PlayListActivity.class);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//
//        } else if (position == 1) {
//            // 添加到歌单
//            createAddToSongListDialog();
//        } else if (position == 2) {
//            // 创建歌曲详细信息显示dialog
//            createSongDetailDialog();
//        } else if (position == 3) {
//            // 删除
//            deleteMusic();
//        }
//    }
//
//
//    private int mChoice = 0;
//
//    /**
//     * 创建一个添加到歌单的dialog
//     */
//    private void createAddToSongListDialog() {
//
//        // 获取所有歌单
//        ArrayList<SongList> songLists = DataBaseManager.getInstance(MusicListActivity.this)
//                .getAllSongList();
//
//        if (songLists == null || songLists.size() == 0) {
//            Toast.makeText(MusicListActivity.this, "歌单还没有被创建", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final String[] items = new String[songLists.size()];
//        for (int i = 0; i < songLists.size(); i++) {
//            items[i] = songLists.get(i).getName();
//        }
//
//        final AlertDialog dialog = DialogUtils.createSingleChoiceDialog(MusicListActivity.this
//                , "歌单列表", items, mChoice
//                , new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 选择了哪一个
//                        mChoice = which;
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 确定按钮
//                        mChoice = mChoice < 0 ? 0
//                                : mChoice >= items.length
//                                ? items.length - 1 : mChoice;
//                        String songList = items[mChoice];
//
//                        // 将当前歌曲加入到当前歌单中
//                        // 先通过歌单名称获取歌单id
//                        int songListId = DataBaseManager.getInstance(MusicListActivity.this)
//                                .getSongListIdByName(songList);
//                        if (songListId != -1 || songListId > 0) {
//
//                            MusicInfo info = getCurrMusic();
//                            Song song = new Song();
//                            song.setSongListId(songListId);
//                            song.setName(info.getMusicName());
//                            song.setSongPath(info.getMusicPath());
//                            song.setSinger(info.getSinger());
//                            // 然后将歌曲添加到该歌单id下
//                            boolean isSuccess = DataBaseManager.getInstance(MusicListActivity.this)
//                                    .insertSong(song);
//
//                            SongList list = DataBaseManager.getInstance(MusicListActivity.this)
//                                    .getSongListById(songListId);
//
//                            int count = list.getCount();
//                            count++;
//                            boolean isUpdate = DataBaseManager.getInstance(MusicListActivity.this)
//                                    .updateSongOfListBySongListId(songListId, count);
//
//                            if (isSuccess && isUpdate) {
//                                Toast.makeText(MusicListActivity.this
//                                        , info.getMusicName() + "被加入到"
//                                                + songList + "歌单中", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 取消按钮
//                        dialog.dismiss();
//                    }
//                });
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//    }
//
//    /**
//     * 删除音乐
//     */
//    private void deleteMusic() {
//        MusicInfo info = getCurrMusic();
//        String path = info.getMusicPath();
//        File musicFile = new File(path);
//        if (!musicFile.exists()) {
//            return;
//        }
//        musicFile.delete();
//        // 删除MediaStore数据库中的数据
//        MusicUtils.deleteMusic(MusicListActivity.this, info.getMusicId());
//        mItems.remove(mSelectionPos);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 创建歌曲详细信息的dialog
//     */
//    private void createSongDetailDialog() {
//        if (mMusicList == null || mMusicList.size() == 0) {
//            return;
//        }
//        View songDetailView = getLayoutInflater().inflate(R.layout.dialog_song_detail, null);
//        TextView tvSongName = (TextView) songDetailView.findViewById(R.id.tv_song_name);
//        TextView tvSongArtist = (TextView) songDetailView.findViewById(R.id.tv_song_artist);
//        TextView tvSongTime = (TextView) songDetailView.findViewById(R.id.tv_song_time);
//        TextView tvSongPath = (TextView) songDetailView.findViewById(R.id.tv_song_path);
//        TextView tvSongAlbum = (TextView) songDetailView.findViewById(R.id.tv_song_album);
//        TextView tvSongSize = (TextView) songDetailView.findViewById(R.id.tv_song_size);
//        Button btnOK = (Button) songDetailView.findViewById(R.id.btn_ok);
//
//        MusicInfo info = getCurrMusic();
//        tvSongName.setText(info.getMusicName());
//        tvSongArtist.setText(info.getSinger());
//        tvSongTime.setText(CommonUtils.stringForTime(info.getTotalTime()));
//        tvSongPath.setText(info.getMusicPath());
//        tvSongAlbum.setText(info.getMusicAlbumsName().equals("0")
//                ? "unknown" : info.getMusicAlbumsName());
//        tvSongSize.setText(Formatter.formatFileSize(this, info.getMusicSize()));
//
//        final AlertDialog dialog = DialogUtils
//                .createContentDialog(MusicListActivity.this, songDetailView);
//
//        dialog.show();
//
//        btnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }
}
