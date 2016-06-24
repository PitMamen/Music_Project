package com.dlighttech.music.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by zhujiang on 16-6-24.
 */
public class DialogUtils {

    public static AlertDialog createContentDialog(Context context, View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        return builder.create();
    }


}
