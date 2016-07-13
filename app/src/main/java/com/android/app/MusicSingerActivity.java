package com.android.app;

import android.graphics.Bitmap;
import android.widget.ListView;
import android.widget.TextView;

import com.allenliu.sidebar.SideBar;
import com.dlighttech.music.adapter.ContentAdapter;
import com.dlighttech.music.model.ContentItem;
import com.dlighttech.music.model.MusicInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pengxinkai001 on 2016/6/24.
 */
public class MusicSingerActivity extends BaseActivity {

    private ListView mListview;
    private SideBar sb_navigation_bar;
    private TextView tv_music_number;

    private ContentAdapter mdapter;

    private ArrayList<ContentItem> items = new ArrayList<ContentItem>();
    private ArrayList<MusicInfo> arrayList;

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


        mdapter = new ContentAdapter(this, items, true);
        mdapter.setMusicInfos(arrayList);
        mListview.setAdapter(mdapter);

        int count = mdapter.getCount();
        tv_music_number.setText("共" + count + "首");


        sb_navigation_bar = (SideBar) findViewById(R.id.navigation_bar);

    }

    @Override
    public void onCreateData() {


        arrayList = MusicUtils.getMusicInfo(this, true);

        for (int i = 0; i < arrayList.size() ; i++) {

            MusicInfo info = arrayList.get(i);

            String musicsiner = info.getSinger();


            int musiccount = info.getSingermusicCount();

            Bitmap bitmap = info.getMusicAlbumsImage();

            ContentItem item;
            if (bitmap!=null){
                item = new ContentItem(bitmap,R.drawable.more_title_selected,musicsiner,musiccount+"首");
            }else {
                item = new ContentItem(R.drawable.singer,R.drawable.more_title_selected,musicsiner,musiccount+"首");
            }
            items.add(item);

        }

        }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchSubmit(String text) {

    }


    /*//去除相同元素的方法
    public static ArrayList<MusicInfo> singleElement(ArrayList<MusicInfo> al) {
        ArrayList<MusicInfo> arrayList = new ArrayList<>();
        Iterator<MusicInfo> it = al.iterator();
        while (it.hasNext()) {
            MusicInfo obj = it.next();
            //如果不包含该元素,则添加进来,contains() 方法底层调用的是 Person 的 equals() 方法
            if (!arrayList.contains(obj))
                arrayList.add(obj);
        }
        //返回新的没有重复元素的ArrayList集合对象
        return arrayList;
    }
*/


}


