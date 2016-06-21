package com.android.music;

import android.app.Activity;
import android.os.Bundle;

public class MusicHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_home_page_layout);

        initActionBar();

    }

    private void initActionBar() {
        // 显示自定义视图
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(R.layout.home_title_layout);
    }
}
