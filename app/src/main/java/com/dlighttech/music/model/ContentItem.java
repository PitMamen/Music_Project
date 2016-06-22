package com.dlighttech.music.model;

import android.graphics.Bitmap;

/**
 * Created by dengyong on 2016/6/21.
 * 内容列表类
 */


public class ContentItem {
    private Bitmap thumb;
    private Bitmap operator;
    private String title;
    private String content;

    /**
     * @param thumb    左侧缩略图（Bitmap）
     * @param operator 右侧操作图（Bitmap）
     * @param title    上部文字
     * @param content  下部文字
     */
    public ContentItem(Bitmap thumb, Bitmap operator, String title, String content) {
        this.thumb = thumb;
        this.operator = operator;
        this.title = title;
        this.content = content;
    }

    public ContentItem(Bitmap thumb, Bitmap operator, String title) {
        this.thumb = thumb;
        this.operator = operator;
        this.title = title;
    }

    public Bitmap getThumb() {

        return thumb;
    }

    public void setThumb(Bitmap imageView) {
        this.thumb = imageView;
    }

    public Bitmap getOperator() {
        return operator;
    }

    public void setOperator(Bitmap operator) {
        this.operator = operator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
