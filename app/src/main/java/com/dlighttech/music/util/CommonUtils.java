package com.dlighttech.music.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by zhujiang on 16-6-24.
 */
public class CommonUtils {

    private static StringBuilder sFormatBuilder = new StringBuilder();
    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale
            .getDefault());

    /**
     * 将音乐时间转换为 hh:mm:ss
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        sFormatBuilder.setLength(0);
        if (hours > 0) {
            return sFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return sFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
