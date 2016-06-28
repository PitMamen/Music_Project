package com.dlighttech.music.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

/**
 * Created by zhujiang on 16-6-24.
 */
public class DialogUtils {

    public static AlertDialog createContentDialog(Context context, View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        return builder.create();
    }

    /**
     * 创建一个单选dialog
     *
     * @param ctx
     * @param title
     * @param items
     * @param defChecked
     * @param singleChoiceClick
     * @param positiveClick
     * @param negativeClick
     * @return
     */
    public static AlertDialog createSingleChoiceDialog(
            Context ctx
            , String title
            , String[] items
            , int defChecked
            , DialogInterface.OnClickListener singleChoiceClick
            , DialogInterface.OnClickListener positiveClick
            , DialogInterface.OnClickListener negativeClick) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, defChecked, singleChoiceClick);
        builder.setPositiveButton("确定", positiveClick);
        builder.setNegativeButton("取消", negativeClick);
        return builder.create();

    }

    /**
     * 创建一个ListPopupWindow
     *
     * @param ctx
     * @param anchor
     * @param listener
     * @return
     */
    public static ListPopupWindow createListPopupWindow(Context ctx
            , String[] menu
            , View anchor
            , AdapterView.OnItemClickListener listener) {
        ListPopupWindow popupWindow = new ListPopupWindow(ctx);
        popupWindow.setWidth(DisplayUtils.dip2px(ctx, 200));
        popupWindow.setHeight(DisplayUtils.dip2px(ctx, 200));
        popupWindow.setAdapter(new ArrayAdapter<String>(ctx
                , android.R.layout.simple_list_item_1, menu));
        popupWindow.setAnchorView(anchor);
        popupWindow.setModal(true);
        popupWindow.setOnItemClickListener(listener);
        popupWindow.setDropDownGravity(Gravity.CENTER);
        return popupWindow;
    }


//    public interface OnChoiceButtonListener {
//        void onPositive();
//
//        void onNegative();
//    }


}
