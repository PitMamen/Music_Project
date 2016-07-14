package com.dlighttech.music.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dengyong on 2016/6/21.
 * 内容列表类
 */


public class ContentItem implements Serializable {
    private int thumb = 0;
    private int operator;
    private String title;
    private String content;
    private Bitmap thumbBitmap;
    private Bitmap operatorBitmap;

    /**
     * @param thumb   左侧缩略图
     * @param operator 右侧操作图（资源id）
     * @param title    上部文字
     * @param content  下部文字
     */
    public ContentItem(Bitmap thumb, int operator, String title, String content) {
        this.operator = operator;
        this.title = title;
        this.content = content;
        this.thumbBitmap = thumb;
    }

    /**
     * @param thumb    左侧缩略图
     * @param operator 右侧操作图（资源id）
     * @param title    上部文字
     * @param content  下部文字
     */
    public ContentItem(Bitmap thumb, Bitmap operator, String title, String content) {
        this.operatorBitmap = operator;
        this.title = title;
        this.content = content;
        this.thumbBitmap = thumb;
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

    public ContentItem(int thumb,String tiltle,String content){
        this.thumb = thumb;
        this.title=tiltle;
        this.content = content;

    }



    public ContentItem() {
    }


    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public Bitmap getThumbBitmap() {
        return thumbBitmap;
    }

    public void setThumbBitmap(Bitmap thumbBitmap) {
        this.thumbBitmap = thumbBitmap;
    }

    public Bitmap getOperatorBitmap() {
        return operatorBitmap;
    }

    public void setOperatorBitmap(Bitmap operatorBitmap) {
        this.operatorBitmap = operatorBitmap;
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

    @Override
    public String toString() {
        return "ContentItem{" +
                "thumb=" + thumb +
                ", operator=" + operator +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
