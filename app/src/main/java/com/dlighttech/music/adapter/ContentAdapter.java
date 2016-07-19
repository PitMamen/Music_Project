package com.dlighttech.music.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.DataChangedWatcher;
import com.android.app.MusicUtils;
import com.android.app.PlayListActivity;
import com.android.app.R;
import com.dlighttech.music.database.DataBaseManager;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;
import com.dlighttech.music.model.SongList;
import com.dlighttech.music.util.CommonUtils;
import com.dlighttech.music.util.DialogUtils;
import com.dlighttech.music.util.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengyong on 2016/6/21.
 * <p/>
 * 继承相应的接口实现对应的点击事件
 */

public class ContentAdapter extends BaseAdapter {
    private List<ContentItem> contentItems;
    private LayoutInflater inflater;
    private Context mContext;
    private OnConvertViewClicked mOnConvertView;
    private OnOperateClicked mOnOperate;
    private OnThumbClicked mOnthumb;
    private ListPopupWindow popupWindow;
    private ArrayList<MusicInfo> mMusicInfos;
    private boolean isHidden = false;
    private boolean isMenu = false;
    private int mChoice = 0;
    private int mSelectPostion = 0;
    private final Activity activity;

    public ContentAdapter(Context context
            , List<ContentItem> lists
            , boolean isMenu) {
        this.inflater = LayoutInflater.from(context);
        this.contentItems = lists;
        this.mContext = context;
        this.isMenu = isMenu;

        activity = (Activity) context;
        if (activity instanceof OnConvertViewClicked) {
            mOnConvertView = (OnConvertViewClicked) activity;
        }
        if (activity instanceof OnThumbClicked) {
            mOnthumb = (OnThumbClicked) activity;
        }
        if (activity instanceof OnOperateClicked) {
            mOnOperate = (OnOperateClicked) activity;
        }
        ;
    }

    public void setIsOperationHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    @Override
    public int getCount() {
        return contentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return contentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_layout,
                    parent, false);
            holder = new ViewHolder();

            holder.thumb = (ImageView) convertView.findViewById(R.id.thumb_imageView_content);
            holder.operator = (ImageView) convertView.findViewById(R.id.operate_imageView_content);
            holder.title = (TextView) convertView.findViewById(R.id.title_content);
            holder.content = (TextView) convertView.findViewById(R.id.content_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ContentItem contentItem = contentItems.get(position);

        Bitmap bmOpr = contentItem.getOperatorBitmap();
        Bitmap bmThumb = contentItem.getThumbBitmap();

        if (bmOpr == null) {
            holder.operator.setImageResource(contentItem.getOperator());
        } else {
            holder.operator.setImageBitmap(bmOpr);
        }

        if (bmThumb == null) {
            holder.thumb.setImageResource(contentItem.getThumb());
        } else {
            holder.thumb.setImageBitmap(bmThumb);
        }

//        if (contentItem.getThumb() == 0) {
//            holder.thumb.setImageBitmap(contentItem.getBitmap());
//        } else {
//            holder.thumb.setImageResource(contentItem.getThumb());
//        }
//        holder.operator.setImageResource(contentItem.getOperator());


        holder.operator.setVisibility(isHidden ? View.GONE : View.VISIBLE);
        holder.title.setText(contentItem.getTitle());
        holder.content.setText(contentItem.getContent());

        MyListener listener = new MyListener(position);
        convertView.setOnClickListener(listener);
        holder.thumb.setOnClickListener(listener);
        if (isMenu()) {

            final int mPostion = position;

            holder.operator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPopupWindowMenu(v, mPostion);
                }
            });
        } else {
            holder.operator.setOnClickListener(listener);
        }

        return convertView;
    }


    /**
     * 创建一个popupWindow菜单
     *
     * @param v
     */
    private void createPopupWindowMenu(View v, int mPosition) {
        mSelectPostion = mPosition;

        String[] menus = {"新建歌单", "添加到歌单", "查看歌曲信息", "删除"};
        popupWindow = DialogUtils.createListPopupWindow(mContext
                , menus, v, mPopupWindowItemClick);
        popupWindow.show();
    }

    private AdapterView.OnItemClickListener mPopupWindowItemClick =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    if (position == 0) {
                        // 新建歌单, 添加到歌单
                        Intent intent = new Intent(mContext, PlayListActivity.class);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else if (position == 1) {
                        // 添加到歌单
                        createAddToSongListDialog();
                    } else if (position == 2) {
                        // 创建歌曲详细信息显示dialog
                        createSongDetailDialog();
                    } else if (position == 3) {
                        // 删除
                        deleteMusic();
                    }
                }
            };


    /**
     * 创建一个添加到歌单的dialog
     */
    private void createAddToSongListDialog() {

        // 获取所有歌单
        ArrayList<SongList> songLists = DataBaseManager.getInstance(mContext)
                .getAllSongList();

        if (songLists == null || songLists.size() == 0) {
            Toast.makeText(mContext, "歌单还没有被创建", Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] items = new String[songLists.size()];
        for (int i = 0; i < songLists.size(); i++) {
            items[i] = songLists.get(i).getName();
        }

        final AlertDialog dialog = DialogUtils.createSingleChoiceDialog(mContext
                , "歌单列表", items, mChoice
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 选择了哪一个
                        mChoice = which;
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确定按钮
                        mChoice = mChoice < 0 ? 0
                                : mChoice >= items.length
                                ? items.length - 1 : mChoice;
                        String songList = items[mChoice];

                        // 将当前歌曲加入到当前歌单中
                        // 先通过歌单名称获取歌单id
                        int songListId = DataBaseManager.getInstance(mContext)
                                .getSongListIdByName(songList);
                        if (songListId != -1 || songListId > 0) {

                            MusicInfo info = getMusicInfo();

                            // 判断当前音乐是否已经添加到歌单中, 同一个歌单中，无论歌曲同名与否
                            // 只能存在一种路径的音乐文件
                            boolean isSamePath = DataBaseManager.getInstance(mContext)
                                    .isExistsSamePathOfSongList(songListId, info.getMusicPath());
                            if (isSamePath) {
                                Toast.makeText(mContext
                                        , info.getMusicPath() + "已经收录到该歌单中"
                                        , Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Song song = new Song();
                            song.setId(info.getMusicId());
                            song.setAlbumId(info.getAlbumId());
                            song.setSongListId(songListId);
                            song.setName(info.getMusicName());
                            song.setSongPath(info.getMusicPath());
                            song.setSinger(info.getSinger());
                            // 然后将歌曲添加到该歌单id下
                            boolean isSuccess = DataBaseManager.getInstance(mContext)
                                    .insertSong(song);

                            SongList list = DataBaseManager.getInstance(mContext)
                                    .getSongListById(songListId);

                            int count = list.getCount();
                            ++count;
                            boolean isUpdate = DataBaseManager.getInstance(mContext)
                                    .updateSongOfListBySongListId(songListId, count);

                            if (isSuccess && isUpdate) {
                                Toast.makeText(mContext
                                        , info.getMusicName() + "被加入到"
                                                + songList + "歌单中", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消按钮
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 删除音乐
     */
    private void deleteMusic() {
        MusicInfo info = getMusicInfo();
        // 是否为歌单删除
        boolean isSongList = PreferencesUtils.getInstance(mContext, PreferencesUtils.SONG_LIST)
                .getBoolean(PreferencesUtils.IS_SONG_LIST_DEL_KEY);

        if (isSongList) {
            // true删除歌单音乐文件
            songListDelOption(info);
        } else {
            // 默认为删除音乐文件
            musicFileDelOption(info);
        }
    }


    private AlertDialog tmpDialog;

    /**
     * 此操作真正将音乐文件删除,如果歌单中也存在该歌曲，则该歌曲也将删除
     *
     * @param info
     */
    private void musicFileDelOption(final MusicInfo info) {
        String path = info.getMusicPath();
        final File musicFile = new File(path);
        if (!musicFile.exists()) {
            return;
        }

        tmpDialog = DialogUtils.createYesOrNoDialog(mContext, "提示", "确定", "取消"
                , "如果删除该歌曲，同时也会将歌单中该歌曲同时删除，确认要删除吗？"
                , new DialogUtils.OnChoiceButtonListener() {
                    @Override
                    public void onPositive() {

                        // 如果删除该歌曲，同时也会将歌单中该歌曲同时删除,如果歌单中没有
                        // 该首歌曲则没有任何操作
                        // 获取已经删除歌曲的列表，每次只可能将一个歌单中的数据删除一次
                        ArrayList<SongList> songLists = DataBaseManager.getInstance(mContext)
                                .deleteSongBySongList(info);

                        for (int i = 0; i < songLists.size(); i++) {
                            SongList list = songLists.get(i);
                            Log.d("TAG", "(更新)getName.get=====" + list.getName());
                            // 当删除歌单中的数据时，需要将count通知观察者更新
                            list.setCount(list.getCount() - 1);
                            DataChangedWatcher.getInstance().update(list);
                        }


                        // 删除音乐文件
                        boolean isFileDel = musicFile.delete();
                        // 删除MediaStore数据库中的数据
                        boolean isMusicDel = MusicUtils.deleteMusic(mContext, info.getMusicId());
                        if (isFileDel && isMusicDel) {
                            Log.d("TAG", "isFileDel====" + isFileDel
                                    + ", isMusicDel======" + isMusicDel);
                            Toast.makeText(mContext
                                    , info.getMusicName() + "已删除！"
                                    , Toast.LENGTH_SHORT).show();
                            contentItems.remove(mSelectPostion);
                            notifyDataSetChanged();
                            tmpDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNegative() {
                        tmpDialog.dismiss();
                    }
                });


        tmpDialog.show();


    }

    /**
     * 在歌单中删除音乐,只删除在歌单表库中的数据，而不是真正将音乐删除
     *
     * @param info
     */
    private void songListDelOption(MusicInfo info) {
        // 删除SongList表下的Song数据
        // 通过歌曲名称获取songListId，通过songListId和songName删除Song表中的数据
        int songListId = PreferencesUtils.getInstance(mContext, PreferencesUtils.SONG_LIST)
                .getInteger(PreferencesUtils.SONG_LIST_ID_KEY);

        boolean isSuccess = DataBaseManager.getInstance(mContext)
                .deleteSongBySongListIdAndName(songListId, info.getMusicName());
        Log.d("TAG", "popupWindow Adapter song delete===" + isSuccess + " songListId===" + songListId);
        // 成功删除song表中的数据
        contentItems.remove(mSelectPostion);
        Toast.makeText(mContext, info.getMusicName() + "删除ok！",
                Toast.LENGTH_SHORT).show();
        // 获取当前songListId下有几首歌曲
        SongList list = DataBaseManager.getInstance(mContext)
                .getSongListById(songListId);

        // 通知数据发生改变
        DataChangedWatcher.getInstance().update(list);

        notifyDataSetChanged();
    }


    /**
     * 创建歌曲详细信息的dialog
     */
    private void createSongDetailDialog() {
        View songDetailView = inflater.inflate(R.layout.dialog_song_detail, null);
        TextView tvSongName = (TextView) songDetailView.findViewById(R.id.tv_song_name);
        TextView tvSongArtist = (TextView) songDetailView.findViewById(R.id.tv_song_artist);
        TextView tvSongTime = (TextView) songDetailView.findViewById(R.id.tv_song_time);
        TextView tvSongPath = (TextView) songDetailView.findViewById(R.id.tv_song_path);
        TextView tvSongAlbum = (TextView) songDetailView.findViewById(R.id.tv_song_album);
        TextView tvSongSize = (TextView) songDetailView.findViewById(R.id.tv_song_size);
        Button btnOK = (Button) songDetailView.findViewById(R.id.btn_ok);

        MusicInfo info = getMusicInfo();
        tvSongName.setText(info.getMusicName());
        tvSongArtist.setText(info.getSinger());
        tvSongTime.setText(CommonUtils.stringForTime(info.getTotalTime()));
        tvSongPath.setText(info.getMusicPath());
        tvSongAlbum.setText(info.getMusicAlbumsName().equals("0")
                ? "unknown" : info.getMusicAlbumsName());
        tvSongSize.setText(Formatter.formatFileSize(mContext, info.getMusicSize()));

        final AlertDialog dialog = DialogUtils
                .createContentDialog(mContext, songDetailView);

        dialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public boolean isMenu() {
        return isMenu;
    }

    /**
     * 设置是否需要popupWindow
     *
     * @return
     */
//    public void setMenu(boolean menu) {
//        isMenu = menu;
//    }
    public void setMusicInfos(ArrayList<MusicInfo> infos) {
        this.mMusicInfos = infos;
    }

    private MusicInfo getMusicInfo() {
        if (mMusicInfos == null || mMusicInfos.size() == 0) {
            throw new IllegalArgumentException("Music info array not be null and size more than 0!");
//            return null;
        }

        return mMusicInfos.get(mSelectPostion);
    }


    private static class ViewHolder {
        ImageView thumb;
        ImageView operator;
        TextView title;
        TextView content;
    }

    private class MyListener implements View.OnClickListener {

        private int position = 0;

        public MyListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.operate_imageView_content:
                    if (mOnOperate != null) {
                        mOnOperate.onOperateClicked(position, v);
                    }
                    break;
                case R.id.thumb_imageView_content:
                    if (mOnthumb != null) {
                        mOnthumb.onThumbClicked(position);
                    }
                    break;
                case R.id.content_layout:
                    if (mOnConvertView != null) {
                        // 保存当前点击的歌曲，存入最近播放的表中
                        saveRecent(position);
                        mOnConvertView.onConvertViewClicked(position);
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 保存最近播放的歌曲
     *
     * @param position
     */
    private void saveRecent(int position) {
        if (mMusicInfos == null || mMusicInfos.size() == 0) return;
        MusicInfo info = mMusicInfos.get(position);
        if (info == null) return;
        // 如果点击播放的id在数据库中存在，则不再添加
        if (DataBaseManager.getInstance(mContext).isExistsRecentSong(info.getMusicId())) {
            boolean isDel = DataBaseManager.getInstance(mContext).deleteRecent(info.getMusicId());
            Log.d("TAG", "删除重复最近歌曲===" + isDel);
        }

        Song song = new Song();
        song.setId(info.getMusicId());
        song.setName(info.getMusicName());
        song.setSinger(info.getSinger());
        song.setTime(System.currentTimeMillis());
        song.setAlbumId(info.getAlbumId());

        if (DataBaseManager.getInstance(mContext).insertRecent(song)) {
            Log.d("TAG", "Save recent successful!!");
            Log.d("TAG", song.toString());
        }
    }

    /**
     * 列表item点击事件
     */
    public interface OnConvertViewClicked {
        void onConvertViewClicked(int position);
    }

    /**
     * 左侧图片点击事件
     */
    public interface OnThumbClicked {
        void onThumbClicked(int position);
    }

    /**
     * 右侧图片点击事件
     */

    public interface OnOperateClicked {
        void onOperateClicked(int position, View v);
    }


}
