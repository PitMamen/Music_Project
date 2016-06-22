package com.dlighttech.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dlighttech.music.model.ContentItem;

import java.util.List;

/**
 * Created by zhujiang on 16-6-22.
 */
public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ContentItem> mItems;

    public HomeAdapter(Context ctx, List<ContentItem> items) {
        mInflater = LayoutInflater.from(ctx);
        mItems = items;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
