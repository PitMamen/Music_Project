package com.dlighttech.music.model;

import java.io.Serializable;

/**
 * Created by dengyong on 2016/6/21.
 * 内容列表类
 */


public class ContentItem implements Serializable{
    private int thumb;
    private int operator;
    private String title;
    private String content;

    public ContentItem() {
    }

    /**
     * @param thumb    左侧缩略图（资源id）
     * @param operator 右侧操作图（资源id）
     * @param title    上部文字
     * @param content  下部文字
     */




    public ContentItem(int thumb, int operator, String title, String content) {
        this.thumb = thumb;
        this.operator = operator;
        this.title = title;
        this.content = content;
    }

    public ContentItem(int thumb, int operator, String title) {
        this.thumb = thumb;
        this.operator = operator;
        this.title = title;
    }

    public int getThumb() {

        return thumb;
    }

    public void setThumb(int imageView) {
        this.thumb = imageView;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
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
