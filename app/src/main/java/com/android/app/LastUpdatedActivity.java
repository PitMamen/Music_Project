package com.android.app;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class LastUpdatedActivity extends BaseActivity implements ContentAdapter.OnConvertViewClicked {

    private ListView mListView;
    private ContentAdapter mAdapter;
    private List<ContentItem> mItems = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();
    private TextView tvPlayMode, tvCount;
    private ImageView ivPlaymode;

    @Override
    public void onCreateView() {
        getWindow().setBackgroundDrawable(getDrawable(android.R.color.white));
        super.setTitleText("最近更新");
        mListView = (ListView) findViewById(R.id.lv_updated_list);
        mAdapter = new ContentAdapter(this, mItems, true);
        mAdapter.setMusicInfos(infos);
        mListView.setAdapter(mAdapter);

        View headView = getLayoutInflater().inflate(R.layout.listview_head_layout, null);
        tvPlayMode = (TextView) headView.findViewById(R.id.tv_play_mode);
        tvCount = (TextView) headView.findViewById(R.id.tv_song_count);
        ivPlaymode = (ImageView) headView.findViewById(R.id.play_mode_icon);
        tvCount.setText(String.valueOf(mAdapter.getCount()));
        ivPlaymode.setOnClickListener(mRepeatClick);
        mListView.addHeaderView(headView);
    }

    private View.OnClickListener mRepeatClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repeatClick(v);
        }
    };


    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        setRepeatButtonImage(ivPlaymode);
    }

    private void repeatClick(View v) {
        super.cycleRepeat((ImageView) v);
    }

    @Override
    public void onInitView() {
        setContentView(R.layout.activity_last_updated);
    }

    @Override
    public void onCreateData() {

        PreferencesUtils.getInstance(this, PreferencesUtils.SONG_LIST)
                .putData(PreferencesUtils.IS_SONG_LIST_DEL_KEY, false);

        ArrayList<MusicInfo> infos = MusicUtils.getMusicInfo(this, true);
        if (infos != null && infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                MusicInfo info = infos.get(i);
                String name = info.getMusicName();
                String singer = info.getSinger();
                Bitmap bitmap = info.getMusicAlbumsImage();
                ContentItem item;
                if (bitmap != null) {
                    item = new ContentItem(bitmap, R.drawable.more_title_selected, name, singer);
                } else {
                    item = new ContentItem(R.drawable.albums_list, R.drawable.more_title_selected, name, singer);
                }

                mItems.add(item);
                this.infos.add(info);
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
        if (infos != null && infos.size() > 0) {

            StringBuilder mSelection = new StringBuilder();
            String[] mSelectionArgs = new String[infos.size()];
            // 由于需要下一曲的播放所以需要将当前目录下的歌曲以游标的形式传递给Service
            for (int i = 0; i < infos.size(); i++) {

                mSelection.append(MediaStore.Audio.Media._ID + "=?");
                mSelection.append(i == infos.size() - 1 ? "" : " or ");
                mSelectionArgs[i] = String.valueOf(infos.get(i).getMusicId());

                Log.d("TAG",infos.get(i).getMusicName());

            }

            super.playCursor(mSelection.toString(), mSelectionArgs, true, position);
        }
    }
}
