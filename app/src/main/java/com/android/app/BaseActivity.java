package com.android.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView tvHomeTitle, tvTitle;
    private RelativeLayout mTitleLayout, mSearchButtonLayout;
    private ImageButton mSearchButton, mReturnButton;
    private View mView;
    private int layoutResId;
    private LinearLayout mActionBar;

    /**
     * 根据提供的是布局资源id还是view来选择加载布局
     *
     * @param contentView
     * @param resId
     */
    public BaseActivity(View contentView, int resId) {
        this.mView = contentView;
        this.layoutResId = resId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mView != null) {
            setContentView(mView);
        } else {
            setContentView(layoutResId);
        }
        // 初始化actionBar
        initActionBar();
        // 初始化数据需要子类重写
        onCreateData();
        // 初始化View需要子类重写
        onCreateView();
    }

    /**
     * 初始化自定义actionBar
     */
    private void initActionBar() {
        mActionBar = (LinearLayout) findViewById(R.id.action_bar_layout);
        if (mActionBar == null) {
            throw new RuntimeException("action bar layout need load!");
        }

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        mSearchButtonLayout = (RelativeLayout) findViewById(R.id.search_btn_layout);
        mSearchButton = (ImageButton) findViewById(R.id.btn_search);
        mReturnButton = (ImageButton) findViewById(R.id.btn_return);
        tvHomeTitle = (TextView) findViewById(R.id.tv_home_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        mSearchView.setOnQueryTextListener(this);
        mSearchButton.setOnClickListener(this);
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

    private void openSearchView() {
        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setIconified(false); // 显示searchView中的editText
        mSearchView.onActionViewExpanded(); // 如果内容为空则显示x按钮，否则不显示
        mTitleLayout.setVisibility(View.GONE);
        mSearchButtonLayout.setVisibility(View.GONE);
    }

    private void closeSearchView() {
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

//    private void coloseInputSoft() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            imm.hideSoftInputFromWindow(mSearchEditText.getApplicationWindowToken(), 0);
//        }
//    }


    public abstract void onCreateView();

    public abstract void onCreateData();

    public abstract void onSearchTextChanged(String text);

    public abstract void onSearchSubmit(String text);


}
