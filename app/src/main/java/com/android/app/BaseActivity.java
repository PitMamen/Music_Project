package com.android.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.music.IMediaPlaybackService;
import com.dlighttech.music.model.MusicInfo;
import com.dlighttech.music.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 所有activity都需要继承该类，需要在继承的activity布局文件中include R.layout.home_title_layout的自定义actionBar布局，
 * 否则将抛出异常
 */
public abstract class BaseActivity extends Activity
        implements View.OnClickListener
        , SearchView.OnQueryTextListener
        , Observer {

    private SearchView mSearchView;
    private EditText mSearchEditText;
    private TextView tvHomeTitle, tvTitle, tvMusicName, tvMusicAuthor;
    private RelativeLayout mTitleLayout, mSearchButtonLayout;
    private ImageButton mReturnButton;
    private LinearLayout mActionBar, mBottomtitl;
    private ImageView mImageViewIcon, mImageViewPause, mImageViewNext, mImageViewsearch;
    private ProgressBar mProgressBar;
    private IMediaPlaybackService mService;
    private Cursor mTrackCursor;
    private MusicUtils.ServiceToken mToken;
    private long mDuration;
    private long mCurrTime;
    private int mPercentage;

//    private static final int PLAYING = 0;
//    private static final int PAUSE = 1;
//    private static final int START = 2;
//    private static final int NEXT = 3;


//    private Handler mHandler = new Handler(Looper.getMainLooper()) {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case PLAYING:
//                    Log.d("TAG", "playing!!");
//                    updateView();
//                    break;
//                case PAUSE:
//                    Log.d("TAG", "pause!!");
//                    break;
//                case START:
//                    Log.d("TAG", "start!!");
//                    break;
//                case NEXT:
//                    Log.d("TAG", "next!!");
//                    break;
//
//            }
//        }
//    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void update(Observable observable, Object data) {
        mPercentage = 0;

        if (data instanceof Integer) {
            mProgressHandler.removeMessages(PLAY);
            mPercentage = (int) data;
            mProgressHandler.sendEmptyMessageDelayed(PLAY, 1000);
        }

        if (data instanceof Boolean) {
            mProgressHandler.removeMessages(PAUSE);
            isPause = (boolean) data;
            mProgressHandler.sendEmptyMessage(PAUSE);
        }
        updateView();
    }

    protected IMediaPlaybackService getService() {
        if (mService == null) {
            setupService();
        }
        return mService;
    }

    /**
     * 子类调用用于控制当前音乐播放模式
     * 如：重复播放全部歌曲，重复播放当前歌曲等
     *
     * @param ivRepeat
     */
    protected void cycleRepeat(ImageView ivRepeat) {
        if (mService == null) {
            return;
        }
        try {
            if (!mService.isPlaying()) {
                return;
            }
            int mode = mService.getRepeatMode();
            if (mode == MediaPlaybackService.REPEAT_NONE) {
                mService.setRepeatMode(MediaPlaybackService.REPEAT_ALL);
                showToast(R.string.repeat_all_notif);
            } else if (mode == MediaPlaybackService.REPEAT_ALL) {
                mService.setRepeatMode(MediaPlaybackService.REPEAT_CURRENT);
                if (mService.getShuffleMode() != MediaPlaybackService.SHUFFLE_NONE) {
                    mService.setShuffleMode(MediaPlaybackService.SHUFFLE_NONE);
                }
                showToast(R.string.repeat_current_notif);
            } else {
                mService.setRepeatMode(MediaPlaybackService.REPEAT_NONE);
                showToast(R.string.repeat_off_notif);
            }
            setRepeatButtonImage(ivRepeat);

        } catch (RemoteException ex) {
        }
    }

    protected void setRepeatButtonImage(ImageView mRepeatButton) {
        if (mService == null) return;
        try {
            switch (mService.getRepeatMode()) {

                case MediaPlaybackService.REPEAT_ALL:
                    mRepeatButton.setImageResource(R.drawable.listloop_mode_playback);
                    break;
                case MediaPlaybackService.REPEAT_CURRENT:
                    mRepeatButton.setImageResource(R.drawable.singleloop_mode_playback);
                    break;
                default:
                    mRepeatButton.setImageResource(R.drawable.ic_mp_repeat_off_btn);
                    break;
            }
            mRepeatButton.setBackground(null);
        } catch (RemoteException ex) {
        }
    }

    /**
     * 设置是否重复播放歌曲
     */
    private void setShuffleButtonImage(ImageView mShuffleButton) {
        if (mService == null) return;
        try {
            switch (mService.getShuffleMode()) {
                case MediaPlaybackService.SHUFFLE_NONE:
                    mShuffleButton.setImageResource(R.drawable.ic_mp_shuffle_off_btn);
                    break;
                case MediaPlaybackService.SHUFFLE_AUTO:
                    mShuffleButton.setImageResource(R.drawable.ic_mp_partyshuffle_on_btn);
                    break;
                default:
                    mShuffleButton.setImageResource(R.drawable.ic_mp_shuffle_on_btn);
                    break;
            }
        } catch (RemoteException ex) {
        }
    }


    private Toast mToast;

    private void showToast(int resid) {
        if (mToast == null) {
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(resid);
        mToast.show();
    }

    protected void updateView() {
        try {
            if (mService == null)
                return;
            Bitmap bm = MusicUtils.getArtwork(this, MusicUtils.getCurrentAudioId()
                    , MusicUtils.getCurrentAlbumId(), true);
//            bm = CommonUtils.createCircleImage(bm, DisplayUtils.dip2px(this, 40));
            mImageViewIcon.setImageBitmap(bm);

            tvMusicName.setText(mService.getTrackName());
            tvMusicAuthor.setText(mService.getArtistName());

            mImageViewPause.setImageResource(mService.isPlaying()
                    ? R.drawable.pause_btn_selector : R.drawable.play_btn_selector);

            mProgressBar.setProgress(mPercentage);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册观察者
        DataChangedWatcher.getInstance().registerObserver(this);
        // 父类方法，用于设置播放的服务
        setupService();
        // 设置contentView
        onInitView();
        // 初始化数据需要子类重写
        onCreateData();
        // 初始化actionBar
        initActionBar();
        // 初始化bottomBar
        initBottomBar();
        // 初始化View需要子类重写,
        onCreateView();

    }


    protected void doPauseResume() {
        try {
            if (mService != null) {
                if (mService.isPlaying()) {
                    mProgressHandler.removeMessages(PAUSE);
                    mService.pause();
                    Message.obtain(mProgressHandler, PAUSE).sendToTarget();
                } else {
                    mProgressHandler.removeMessages(PLAY);
                    mService.play();
                    Message.obtain(mProgressHandler, PLAY).sendToTarget();
                }
                setPauseButtonImage();
            }
        } catch (RemoteException ex) {
        }
    }

    private void setPauseButtonImage() {
        try {
            mImageViewPause.setImageResource(mService.isPlaying()
                    ? R.drawable.pause_btn_selector : R.drawable.play_btn_selector);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子类调用，获取当前目录的歌曲列表开始播放音乐
     *
     * @param isOrder
     * @param position
     */
    protected void playCursor(ArrayList<MusicInfo> mMusicList, boolean isOrder, int position) {

        if (mMusicList == null || mMusicList.size() == 0) {
            return;
        }
        StringBuilder selection = new StringBuilder();
        String[] selectionArgs = new String[mMusicList.size()];
        // 由于需要下一曲的播放所以需要将当前目录下的歌曲以游标的形式传递给Service
        for (int i = 0; i < mMusicList.size(); i++) {

            selection.append(MediaStore.Audio.Media._ID + "=?");
            selection.append(i == mMusicList.size() - 1 ? "" : " or ");

            selectionArgs[i] = String.valueOf(mMusicList.get(i).getMusicId());
        }
        Log.d("TAG", selection.toString());

        /* ====== */


        mTrackCursor = MusicUtils.getMusicInfo(this, isOrder, selection.toString()
                , selectionArgs);

        if (mTrackCursor.getCount() == 0) {
            return;
        }

        if (mTrackCursor instanceof TrackBrowserActivity.NowPlayingCursor) {
            if (MusicUtils.sService != null) {
                try {
                    MusicUtils.sService.setQueuePosition(position);
                    return;
                } catch (RemoteException ex) {
                }
            }
        }

        MusicUtils.playAll(this, mTrackCursor, position);

        DataChangedWatcher.getInstance().update();
    }

    protected void setupService() {
        mToken = MusicUtils.bindToService(this, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IMediaPlaybackService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                finish();
            }
        });
    }

    /**
     * 此方法将取消绑定的服务，如果子类重写了onDestory()方法，也需要再次调用此方法
     * 防止因为服务过多，导致意外情况
     */
    protected void unBindService() {
        MusicUtils.unbindFromService(mToken);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mService == null) {
            return;
        }
        try {
            // 将音乐id 、 专辑id、歌曲名称、歌手、当前时间缓存下来，以便初始化时使用
            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.IS_PLAYING, mService.isPlaying());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.SONG_ID, MusicUtils.getCurrentAudioId());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.ALBUM_ID, MusicUtils.getCurrentAlbumId());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.MUSIC_NAME, mService.getTrackName());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.SINGER, mService.getArtistName());

            Log.d("TAG", "updateview百分比：" + mPercentage);

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.CURR_TIME, mPercentage);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 初始化bottomBar
    private void initBottomBar() {
        mBottomtitl = (LinearLayout) findViewById(R.id.bottom_title_bar);
        if (mBottomtitl == null) {
            throw new RuntimeException("bottom bar layout need load!");
        }
        mProgressBar = (ProgressBar) mBottomtitl.findViewById(R.id.pro_music_schedule);
        mImageViewIcon = (ImageView) mBottomtitl.findViewById(R.id.iv_music_icon);
        tvMusicName = (TextView) mBottomtitl.findViewById(R.id.tv_music_name);
        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);
        mImageViewPause = (ImageView) findViewById(R.id.iv_music_play_pause);
        mImageViewNext = (ImageView) mBottomtitl.findViewById(R.id.iv_music_next);

        initBottomViewData();
        mImageViewPause.setOnClickListener(mPauseAndStartListener);
        mImageViewNext.setOnClickListener(mNextListener);

        mImageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressHandler.removeMessages(PLAY);

                Intent intent = new Intent(BaseActivity.this, MediaPlaybackActivity.class);
                startActivity(intent);
            }
        });
    }


    private View.OnClickListener mPauseAndStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mService == null) return;
            try {
                mService.next();
                mProgressHandler.removeMessages(PLAY);
                Message.obtain(mProgressHandler, PLAY).sendToTarget();
                updateView();
            } catch (RemoteException ex) {

            }
        }
    };

    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private boolean isPause = false;
//    private static final int PLAYING = 2;


    private Handler mProgressHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY:
                    if (!isPause) {
                        refreshProgress();
                        mProgressHandler.sendEmptyMessageDelayed(PLAY, 1000); //延迟1秒再次发送
                    } else {
                        mProgressHandler.removeMessages(PLAY);
                    }
                    isPause = false;
                    break;
                case PAUSE:
                    isPause = true;
                    break;
                default:
                    break;
            }
        }
    };

    private void refreshProgress() {
        // 开始计算当前音乐播放的时间以及位置
        try {
            Log.d("TAG", "总时间：" + MusicUtils.makeTimeString(this, mService.duration() / 1000));
            Log.d("TAG", "当前时间：" + MusicUtils.makeTimeString(this, mService.position() / 1000));
            mCurrTime = mService.position();
            mDuration = mService.duration();

            mPercentage = (int) (mCurrTime * 1000.0F / mDuration);
            Log.d("TAG", "百分比：" + mPercentage);
            mProgressBar.setProgress(mPercentage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }


    /**
     * 初始化底部布局的数据
     */
    private void initBottomViewData() {

        long songId = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getLong(PreferencesUtils.SONG_ID);

        long albumId = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getLong(PreferencesUtils.ALBUM_ID);

        Bitmap bm = MusicUtils.getArtwork(this, songId, albumId, true);

//            bm = CommonUtils.createCircleImage(bm, DisplayUtils.dip2px(this, 40));
        mImageViewIcon.setImageBitmap(bm);

        String musicName = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getStr(PreferencesUtils.MUSIC_NAME);

        musicName = TextUtils.isEmpty(musicName)
                ? MediaStore.UNKNOWN_STRING : musicName;

        tvMusicName.setText(musicName);


        String singer = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getStr(PreferencesUtils.SINGER);

        singer = TextUtils.isEmpty(singer) ? MediaStore.UNKNOWN_STRING
                : singer;

        tvMusicAuthor.setText(singer);

        mProgressBar.setMax(1000);

        int progress = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getInteger(PreferencesUtils.CURR_TIME);

        mProgressBar.setProgress(progress);

        boolean isPlaying = PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                .getBoolean(PreferencesUtils.IS_PLAYING);
        if (isPlaying) {
            mImageViewPause.setImageResource(R.drawable.pause_btn_selector);
        } else {
            mImageViewPause.setImageResource(R.drawable.play_btn_selector);
        }

        mImageViewNext.setImageResource(R.drawable.next_btn_selector);
    }

    /**
     * 初始化自定义actionBar
     */
    private void initActionBar() {
        mActionBar = (LinearLayout) findViewById(R.id.action_bar_layout);

        if (mActionBar == null) {
            throw new RuntimeException("action bar layout need load!");
        }
        mSearchView = (SearchView) mActionBar.findViewById(R.id.searchView);
        mTitleLayout = (RelativeLayout) mActionBar.findViewById(R.id.title_layout);
        mSearchButtonLayout = (RelativeLayout) mActionBar.findViewById(R.id.search_btn_layout);
        mImageViewsearch = (ImageView) mActionBar.findViewById(R.id.btn_search);
        mReturnButton = (ImageButton) mActionBar.findViewById(R.id.btn_return);
        tvHomeTitle = (TextView) mActionBar.findViewById(R.id.tv_home_title);
        tvTitle = (TextView) mActionBar.findViewById(R.id.tv_title);

        mSearchView.setOnQueryTextListener(this);
        mImageViewsearch.setOnClickListener(this);
        mReturnButton.setOnClickListener(this);
        // 判断如果是首页，显示左边title，隐藏中心的textview和返回ImageButton
        // 如果不是，则显示左边返回imagebutton,中心的textview,隐藏左边的title
        if (this instanceof MusicHomeActivity) {
            tvHomeTitle.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            mReturnButton.setVisibility(View.GONE);
        } else {
            tvHomeTitle.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            mReturnButton.setVisibility(View.VISIBLE);
        }

        // 去掉search icon
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        // 去掉searchButtonId
        int searchButtonId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchButton = (ImageView) mSearchView.findViewById(searchButtonId);
        searchButton.setVisibility(View.GONE);

        // 去掉最外围的背景框
        int bglayoutid = mSearchView.getContext().getResources()
                .getIdentifier("android:id/search_bar", null, null);
        LinearLayout search_bar = (LinearLayout) mSearchView
                .findViewById(bglayoutid);
        if (search_bar != null) {
            search_bar.setBackgroundColor(Color.TRANSPARENT);
        }

        // 获取EditText
        int editTextId = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        mSearchEditText = (EditText) mSearchView.findViewById(editTextId);
        mSearchEditText.setTextSize(14);
        mSearchEditText.setTextColor(getResources().getColor(R.color.text_color_white));
        mSearchEditText.setHint("输入你的搜索内容");
        mSearchEditText.setHintTextColor(getResources().getColor(R.color.text_color_white));
    }

    protected void setTitleText(String text) {
        tvTitle.setText(text);
    }

    private void openSearchView() {
        tvTitle.setVisibility(View.GONE);
        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setIconified(false); // 显示searchView中的editText
        mSearchView.onActionViewExpanded(); // 如果内容为空则显示x按钮，否则不显示
        mTitleLayout.setVisibility(View.GONE);
        mSearchButtonLayout.setVisibility(View.GONE);
    }

    private void closeSearchView() {
        if (this instanceof MusicHomeActivity == false) {
            tvTitle.setVisibility(View.VISIBLE);
        }
        mSearchView.setIconified(true);
        mSearchView.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.VISIBLE);
        mSearchButtonLayout.setVisibility(View.VISIBLE);
        mSearchView.clearFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                openSearchView();
                break;
            case R.id.btn_return:
                finish();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        // 当点击返回键位时触发 false 表示当前search显示出来，否则不显示
        if (!mSearchView.isIconified()) {
            toggleSearchView();
        } else {
            finish();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        onSearchTextChanged(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onSearchSubmit(query);
        return true;
    }

    private void toggleSearchView() {
        if (mSearchView.isIconified()) {
            openSearchView();
        } else {
            closeSearchView();
        }
    }


//    protected void setCurrPlayingCursor(Cursor mCurrCursor) {
//        if (mCurrCursor == null) {
//            throw new IllegalArgumentException("cursor is not be null,you must setup!");
//        }
//        mTrackCursor = mCurrCursor;
//    }


    // 创建子类的view
    public abstract void onCreateView();

    // 初始化contentView
    public abstract void onInitView();

    // 初始化数据
    public abstract void onCreateData();

    // serachView文本发生改变将触发
    public abstract void onSearchTextChanged(String text);

    // searchView提交时触发
    public abstract void onSearchSubmit(String text);


}
