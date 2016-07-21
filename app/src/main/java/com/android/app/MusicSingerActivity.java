package com.android.app;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.model.Song;
import com.dlighttech.music.util.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicSingerActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListview;
    private SideBar sb_navigation_bar;
    private TextView tv_music_number;

    private ContentAdapter madapter;
    private Cursor cursor;
    private MusicUtils.ServiceToken token;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> arrayList;
    private ArrayList<Song> songs;
    private StringBuilder selecte;
    private String[] selecteArgs;

    @Override
    public void onInitView() {

        setContentView(R.layout.music_singer_layout);

    }

    @Override
    public void onCreateView() {

        super.setTitleText("Singer");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);
        mListview = (ListView) findViewById(R.id.lv_music_detail);
        tv_music_number = (TextView) findViewById(R.id.tv_music_num);


        madapter = new ContentAdapter(this, items, true);
        madapter.setMusicInfos(arrayList);
        mListview.setAdapter(madapter);

        int count = madapter.getCount();
        tv_music_number.setText("共" + count + "名艺术家");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);

    }

    @Override
    public void onCreateData() {

        //long[] ids = MusicUtils.getAllSongs(this);
        songs = MusicUtils.getAllArtist(this);

        for (int i = 0; i < songs.size(); i++) {
            // 获取所有歌曲的id
            Song song = songs.get(i);

            String singer = song.getSinger();

            Log.d("haha", "歌手 id ====" + song.getArtistId());
            Log.d("haha", "歌手 ====" + song.getSinger());


            selecte = new StringBuilder();
            selecteArgs = new String[songs.size()];

            selecte.append(MediaStore.Audio.Media.ARTIST_ID + " =? " );
            selecte.append(i == songs.size() - 1 ? "" : " or ");


            selecteArgs[i] = String.valueOf(songs.get(i).getArtistId());

            arrayList = MusicUtils.getMusicInfo(this,selecte.toString(),  selecteArgs,  false);
        }


        Log.d("bibi", "arrayList.size: " + arrayList.size());

        for (int j = 0; j < arrayList.size(); j++) {
            MusicInfo info = arrayList.get(j);

            String singername = info.getSinger();
            int musicCount = info.getSingermusicCount();

            Bitmap bitmap = info.getMusicAlbumsImage();

            ContentItem item = null;
            if (bitmap != null) {
                item = new ContentItem(bitmap, R.drawable.more_title_selected, singername, musicCount + "首");
            } else {
                item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, singername, musicCount + "首");
            }
            items.add(item);


        }

//        int count = 0;
//
//        for (int i = 0; i < ids.length; i++) {
//            // 获取所有歌曲的id
//            long id = ids[i];
//            Log.d("haha", "song id ====" + id);
//
//            long[] artistIds = MusicUtils.getSongListForArtist(this, id);
//            final Long[] tmpIds = new Long[artistIds.length];
//            for (int k = 0; k < artistIds.length; k++) {
//                tmpIds[k] = artistIds[k];
//            }
//
//            List<Long> artistList = Arrays.asList(tmpIds);
//
//            StringBuilder selection = new StringBuilder();
//            String[] selectionArgs = new String[artistIds.length];
//
//            long mId = -1;
//            for (int j = 0; j < artistList.size(); j++) {
//                // 获取所有歌曲的歌手id
//                long artistId = artistList.get(j);
//                if (artistList.contains(mId)) {
//                    continue;
//                }
//
//                selection.append(MediaStore.Audio.Media.ARTIST_ID + "=?");
//                selection.append(j == artistIds.length - 1 ? "" : " or ");
//                selectionArgs[j] = String.valueOf(artistIds[j]);
//                Log.d("haha", "singer id ====" + artistIds[j]);
//
//                MusicInfo info =arrayList.get(j);
//
//                String name = info.getSinger();
//                int countnumber = info.getSingermusicCount();
//
//                ContentItem item = new ContentItem(R.drawable.singer,R.drawable.more_title_selected,name,countnumber+"首");
//
//                items.add(item);
//
//
//
//                mId = artistId;
//
//
//            }
//
//            arrayList = MusicUtils.getMusicInfo(this, selection.toString(), selectionArgs, false);
//
//
//        }


//        arrayList = MusicUtils.getMusicInfo(this,false);
//
//
//        String singer = null;
//        for (int i = 0; i < arrayList.size(); i++) {
//
//
//            MusicInfo info = arrayList.get(i);
//            Log.d("TAG", info.toString());
//            //    Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getArtistId());
//
//            long[] artisIds = MusicUtils.getSongListForArtist(this, info.getArtistId());
//            Bitmap bitmap = info.getMusicAlbumsImage();
//
//            if (info.getSinger().equals(singer)) {
//                continue;
//            }
//
//            ContentItem item;
//            if (bitmap != null) {
//                item = new ContentItem(bitmap, R.drawable.more_title_selected, info.getSinger(), artisIds.length + "首");
//
//
//            } else {
//                item = new ContentItem(R.drawable.singer, R.drawable.more_title_selected, info.getSinger(), artisIds.length + "首");
//            }
//
//            singer = info.getSinger();
//
//            items.add(item);
//
//        }


//        String albumName = "";
//        for (int i = 0; i < arrayList.size(); i++) {
//
//            MusicInfo info = arrayList.get(i);
//            Log.d("TAG", info.toString());
//
//            Bitmap bitmap = MusicUtils.getArtwork(this, info.getMusicId(), info.getAlbumId(), true);
//            long[] songsIds = MusicUtils.getSongListForAlbum(this, info.getAlbumId());
//
//            if (info.getMusicAlbumsName().equals(albumName)) {
//                continue;
//            }
//            ContentItem item = new ContentItem(bitmap, R.drawable.c_right
//                    , info.getMusicAlbumsName(), songsIds.length + "首");
//
//
//            albumName = info.getMusicAlbumsName();
//            items.add(item);
//        }


    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }

    @Override
    public void onConvertViewClicked(int position) {
         /*MusicInfo info = arrayList.get(position);
        cursor = MusicUtils.getMusicInfo(this, false, MediaStore.Audio.Media._ID + "=?"
                , new String[]{String.valueOf(info.getMusicId())});

        if (cursor.getCount() == 0) {
            return;
        }
        if (cursor instanceof TrackBrowserActivity.NowPlayingCursor) {
            if (MusicUtils.sService != null) {
                try {
                    MusicUtils.sService.setQueuePosition(position);
                    return;
                } catch (RemoteException ex) {
                }
            }
        }
        //播放音乐
        MusicUtils.playAll(this, cursor);*/

        super.playCursor(arrayList, false, position);
    }
}


