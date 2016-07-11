package com.android.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * 所有activity都需要继承该类，需要在继承的activity布局文件中include R.layout.home_title_layout的自定义actionBar布局，
 * 否则将抛出异常
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private EditText mSearchEditText;
    private TextView tvHomeTitle, tvTitle, tvMusicName, tvMusicAuthor;
    private RelativeLayout mTitleLayout, mSearchButtonLayout;
    private ImageButton  mReturnButton;
    private LinearLayout mActionBar, mBottomtitl;
    private ImageView mImageViewIcon, mImageViewPause, mImageViewplay,mImageViewsearch;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置contentView
        onInitView();
        // 初始化数据需要子类重写
        onCreateData();
        // 初始化actionBar
        initActionBar();
        initbottomBar();
        // 初始化View需要子类重写,
        onCreateView();
    }

    // 初始化bottomBar
    private void initbottomBar() {
        mBottomtitl = (LinearLayout) findViewById(R.id.bottom_title_bar);
        if (mBottomtitl == null) {
            throw new RuntimeException("bottom bar layout need load!");
        }
        mProgressBar = (ProgressBar) mBottomtitl.findViewById(R.id.pro_music_schedule);
        mImageViewIcon = (ImageView) mBottomtitl.findViewById(R.id.iv_music_icon);
        tvMusicName = (TextView) mBottomtitl.findViewById(R.id.tv_music_name);
        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);
        mImageViewPause = (ImageView) findViewById(R.id.iv_music_play_pause);
        mImageViewplay = (ImageView) mBottomtitl.findViewById(R.id.iv_music_next);


        mImageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, MediaPlaybackActivity.class);
                startActivity(intent);
            }
        });

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
