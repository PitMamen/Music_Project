package com.dlighttech.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.app.R;
import com.dlighttech.music.model.ContentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页list适配器
 * Created by zhujiang on 16-6-22.
 */
public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ContentItem> mItems;
    private static final int DIR_LAYOUT = 0;
    private static final int COMMON_LAYOUT = 1;
    private ProgressBar mProgress;
    private List<ImageView> imageViews;

    public HomeAdapter(Context ctx, List<ContentItem> items) {
        mInflater = LayoutInflater.from(ctx);
        mItems = items;
        imageViews = new ArrayList<ImageView>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DIR_LAYOUT;
        }
        return COMMON_LAYOUT;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContentItem item = mItems.get(position);
        holder.ivLeft.setImageResource(item.getThumb());
        // 如果首页列表为第一项，则初始设置为加载图片
        if (DIR_LAYOUT == getItemViewType(position)) {
            holder.ivRight.setImageResource(item.getOperator());
            holder.ivRight.setVisibility(View.GONE);
            mProgress = (ProgressBar) convertView.findViewById(R.id.progress);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            holder.ivRight.setImageResource(item.getOperator());
        }
        holder.tvName.setText(item.getTitle());
        imageViews.add(holder.ivRight);
        return convertView;
    }

    /**
     * 刷新完成，更新itemview
     */
    public void updateItem() {
        imageViews.get(0).setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }



//    private void startRotateAnimation(ImageView ivRight) {
//        loadAnimation = new RotateAnimation(0f, 359f
//                , 50f, 50f);
//        loadAnimation.setDuration(500);
//        loadAnimation.setInterpolator(new LinearInterpolator());
//        loadAnimation.setRepeatCount(Animation.INFINITE);
//        loadAnimation.setFillAfter(true);
//        loadAnimation.setFillEnabled(true);
//        ivRight.startAnimation(loadAnimation);
//    }
//
//
//    private void stopRotateAnimation(ImageView ivRight) {
//        ivRight.clearAnimation();
//        loadAnimation.setFillAfter(true);
//        loadAnimation.reset();
//        loadAnimation = null;
//    }


    class ViewHolder {

        ImageView ivLeft, ivRight;
        TextView tvName;

        public ViewHolder(View itemView) {
            ivLeft = (ImageView) itemView.findViewById(R.id.thumb_imageView_content);
            ivRight = (ImageView) itemView.findViewById(R.id.operate_imageView_content);
            tvName = (TextView) itemView.findViewById(R.id.title_content);
        }
    }


}
