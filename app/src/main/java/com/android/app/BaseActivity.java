package com.android.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.music.IMediaPlaybackService;
import com.dlighttech.music.util.PreferencesUtils;

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
    public void update(Observable observable, Object data) {
        updateView();
    }

    private void updateView() {
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
            // 将音乐id 、 专辑id、歌曲名称、歌手、当前时间、总时间
            // 缓存下来，以便初始化时使用
            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.SONG_ID, MusicUtils.getCurrentAudioId());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.ALBUM_ID, MusicUtils.getCurrentAlbumId());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.MUSIC_NAME, mService.getTrackName());

            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.SINGER, mService.getArtistName());


            // 开始计算当前音乐播放的时间以及位置
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

    private View.OnClickListener mNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mService == null) return;
            try {
                mService.next();
            } catch (RemoteException ex) {

            }
        }
    };


    private View.OnClickListener mPauseAndStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    protected void doPauseResume() {
        try {
            if (mService != null) {
                if (mService.isPlaying()) {
                    mService.pause();
                } else {
                    mService.play();
                }
//                setPauseButtonImage();
            }
        } catch (RemoteException ex) {
        }
    }

    /**
     * 子类调用，获取当前目录的歌曲列表开始播放音乐
     *
     * @param selection
     * @param selectionArgs
     * @param isOrder
     * @param position
     */
    protected void playCursor(String selection, String[] selectionArgs
            , boolean isOrder, int position) {

        mTrackCursor = MusicUtils.getMusicInfo(this, isOrder, selection
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
        try {
            PreferencesUtils.getInstance(this, PreferencesUtils.MUSIC)
                    .putData(PreferencesUtils.IS_PLAYING, mService.isPlaying());
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

        mImageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, MediaPlaybackActivity.class);
                startActivity(intent);
            }
        });
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
        mProgressBar.setProgress(0);

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
