package com.dlighttech.music.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.android.app.R;
import com.dlighttech.music.model.ContentItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {
    private static String TAG = "FileUtils";
    private static int KB_TYPE = 1024;
    private static int MB_TYPE = 1024 * 1024;
    private static int GB_TYPE = 1024 * 1024 * 1024;

    /*
     * 根据不同的标志，来获取不同的参数，来查询文件
     */
    public final static int Music = 0;
    public final static int Video = 1;
    public final static int Picture = 2;
    public final static int Doc = 3;
    public final static int Zip = 4;
    public final static int Apk = 5;

    /*
     * 根据不同的类型进行排序,预留位置
     */
    public final static int name = 7;
    public final static int size = 8;
    public final static int date = 9;
    public final static int type = 10;

    /**
     * 将歌曲的时间转换为分秒的制度
     *
     * @param time
     * @return
     */
    public static String formatTime(Long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if (min.length() < 2)
            min = "0" + min;
        switch (sec.length()) {
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static ArrayList<ContentItem> getFileList(Context ctx, int Type) {
        ArrayList<ContentItem> list = new ArrayList<ContentItem>();
        Uri uri = getContentUriByCategory(Type);
        String selection = buildSelection(Type);
        String[] ArgsArr = buildSelectionArgs(Type);
        String[] columns = null;
        Log.d(TAG, "selection==" + selection);
        Log.d(TAG, "ArgsArr==" + ArgsArr);
        Cursor cursor = ctx.getContentResolver().query(uri, columns, selection,
                ArgsArr, null);
        Log.d(TAG, "count:" + cursor.getCount());
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor
                        .getColumnIndexOrThrow((FileColumns.DISPLAY_NAME))); // 文件名称

                int Size = cursor.getInt(cursor
                        .getColumnIndexOrThrow((FileColumns.SIZE))); // 文件大小
                String Data = cursor.getString(cursor
                        .getColumnIndexOrThrow((FileColumns.DATA))); // 路径

                if (Type == Music) {
                    // name , mimetype , size , time ,modifityTime ,path
                    long duration = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(Audio.Media.DURATION)); // 时长

                    ContentItem item = new ContentItem(R.drawable.app_music
                            , R.drawable.left, name, Data);
                    list.add(item);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

	

	/*
     * 获取各类文件的数目
	 */

    public static int getFileCount(Context ctx, int Type) {
        int count = 0;
        Uri uri = getContentUriByCategory(Type);
        String selection = buildSelection(Type);
        String[] ArgsArr = buildSelectionArgs(Type);
        String[] columns = null;
        Cursor cursor = ctx.getContentResolver().query(uri, columns, selection,
                ArgsArr, null);
        Log.d(TAG, "getCount():" + cursor.getCount());
        try {
            if (Type == Apk) {

                while (cursor.moveToNext()) {
                    String Data = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow((FileColumns.DATA)));
                    PackageManager pm = ctx.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(Data,
                            PackageManager.GET_ACTIVITIES);
                    if (info != null) {
                        ApplicationInfo appInfo = info.applicationInfo;
                        appInfo.sourceDir = Data;
                        appInfo.publicSourceDir = Data;
                        try {

                            String Label_name = appInfo.loadLabel(pm)
                                    .toString();
                            String Package_name = appInfo.packageName;
                            String Version = info.versionName == null ? "0"
                                    : info.versionName;

                            Drawable mDrawable = appInfo.loadIcon(pm);
                            if (!TextUtils.isEmpty(Version)
                                    && !TextUtils.isEmpty(Label_name)
                                    && !TextUtils.isEmpty(Package_name)
                                    && mDrawable != null) {
                                count++;
                            }

                        } catch (OutOfMemoryError e) {
                            Log.e("ApkIconLoader", e.toString());
                        }
                    }
                }
            } else {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "count===" + count);

        return count;
    }

    /*
     * 获取各类文件所占的容量大小
     */
//    public static ClassificationFileSize getClassificationInfo(Context ctx) {
//        ClassificationFileSize mClassificationFileSize = new ClassificationFileSize();
//
//        mClassificationFileSize.Apk_Size = getClassificationFileSize(ctx, Apk);
//        mClassificationFileSize.Doc_size = getClassificationFileSize(ctx, Doc);
//        mClassificationFileSize.Image_Size = getClassificationFileSize(ctx,
//                Picture);
//        mClassificationFileSize.Music_size = getClassificationFileSize(ctx,
//                Music);
//        mClassificationFileSize.Video_Size = getClassificationFileSize(ctx,
//                Video);
//        mClassificationFileSize.Zip_size = getClassificationFileSize(ctx, Zip);
//
//        mClassificationFileSize.HaveUsedSize = getRomTotalSize(ctx)
//                - getRomAvailableSize(ctx);
//        mClassificationFileSize.Other_Size = mClassificationFileSize.HaveUsedSize
//                - mClassificationFileSize.Apk_Size
//                - mClassificationFileSize.Doc_size
//                - mClassificationFileSize.Image_Size
//                - mClassificationFileSize.Music_size
//                - mClassificationFileSize.Video_Size
//                - mClassificationFileSize.Zip_size;
//
//        return mClassificationFileSize;
//    }

    private static long getClassificationFileSize(Context ctx, int Type) {
        long Size = 0;
        Uri uri = getContentUriByCategory(Type);
        String selection = buildSelection(Type);
        String[] ArgsArr = buildSelectionArgs(Type);
        String[] columns = null;
        Cursor cursor = ctx.getContentResolver().query(uri, columns, selection,
                ArgsArr, null);
        Log.d(TAG, "getCount():" + cursor.getCount());
        while (cursor.moveToNext()) {

            long File_size = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow((FileColumns.SIZE)));
            if (Type == Apk) {
                String Data = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow((FileColumns.DATA)));
                PackageManager pm = ctx.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(Data,
                        PackageManager.GET_ACTIVITIES);
                if (info != null) {
                    ApplicationInfo appInfo = info.applicationInfo;
                    String sourceDir = Data;
                    appInfo.publicSourceDir = Data;
                    try {

                        String Label_name = appInfo.loadLabel(pm).toString();
                        String Package_name = appInfo.packageName;
                        String Version = info.versionName == null ? "0"
                                : info.versionName;

                        if (!TextUtils.isEmpty(Version)
                                && !TextUtils.isEmpty(Label_name)
                                && !TextUtils.isEmpty(Package_name)) {
                            Size = Size + File_size;
                        }

                    } catch (OutOfMemoryError e) {
                        Log.e("ApkIconLoader", e.toString());
                    }
                }
            } else {
                Size = Size + File_size;
            }
        }

        Log.d(TAG, "Size===" + Formatter.formatFileSize(ctx, Size));
        return Size;
    }

    /*
     * 判断apk是否已经安装
     */
//    private static boolean isApkInstalled(String packagename, Context ctx) {
//
//        PackageManager localPackageManager = ctx.getPackageManager();
//        try {
//
//            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(
//                    packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
//            return true;
//        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
//            return false;
//        }
//
//    }


    private static String buildSelection(int Type) {
        String selection = null;
        switch (Type) {

            case Doc:
                selection = FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=?";
                break;
            case Zip:
                selection = FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=? or "
                        + FileColumns.MIME_TYPE + "=?";
                // selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
                break;
            case Apk:
                selection = FileColumns.DATA + " LIKE '%.apk'";
                break;

            default:
                selection = null;
        }
        return selection;
    }

    /*
     * 根据不同的type类型，获取不同的参数类型
     */
    private static String[] buildSelectionArgs(int Type) {
        String[] Args = null;
        String[] DocArr = {
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/css", "text/html", "text/plain", "text/xml",
                "text/x-java", "text/x-tex"};
        String[] ZipArr = {"application/zip", "application/rar",
                "application/x-iso9660-image", "application/x-gzip",
                "application/x-gtar"};

        String[] VideoArr = {"video/mp4", "video/x-msvideo",
                "video/x-sgi-movie", "video/quicktime", "video/mpeg",
                "video/x-ms-asf"};
        String[] ApkArr = {"application/vnd.android.package-archive"};
        switch (Type) {
            case Doc:
                Args = DocArr;
                break;
            case Zip:
                Args = ZipArr;
                break;
            // case Apk:
            // Args = ApkArr;
            // break;
            default:
                Args = null;
        }
        return Args;
    }

    private String buildSortOrder(int sort) {
        String sortOrder = null;
        switch (sort) {
            case name:
                sortOrder = FileColumns.TITLE + " asc";
                break;
            case size:
                sortOrder = FileColumns.SIZE + " asc";
                break;
            case date:
                sortOrder = FileColumns.DATE_MODIFIED + " desc";
                break;
            case type:
                sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE
                        + " asc";
                break;
        }
        return sortOrder;
    }

    /*
     * 根据文件类型查询对应的文件信息
     */
    public static void getInfoByMimeType(Context context, String mimeType) {
        String volumeName = "external";
        if (TextUtils.isEmpty(mimeType)) {
            return;
        }
        ContentResolver mResolver = context.getContentResolver();
        Uri uri = Files.getContentUri(volumeName);

        String[] projection = {FileColumns.TITLE,
                FileColumns.SIZE,
                FileColumns.DATA};

        String selection = FileColumns.MIME_TYPE + "=?";
        Cursor c = mResolver.query(uri, projection, selection,
                new String[]{mimeType}, null);
        if (c == null)
            return;

        String title = null, data = null, date = null;
        int _id = 0, _count = 0;
        long size = 0L;

        while (c.moveToNext()) {
            title = c.getString(c.getColumnIndex(projection[0]));
            size = c.getLong(c.getColumnIndex(projection[1]));
            data = c.getString(c.getColumnIndex(projection[2]));

            Log.d("TAG", "_id=" + _id + ",title=" + title + ",_count=" + _count
                    + ",size=" + size + ",data=" + data + ",date=" + date);
        }
        if (!c.isClosed()) {
            c.close();
        }
    }

    /*
     * 根据不同类型获取对应的URI
     */
    private static Uri getContentUriByCategory(int cat) {
        Uri uri;
        String volumeName = "external";
        // uri = Files.getContentUri(volumeName);
        switch (cat) {
            case Doc:
            case Zip:
            case Apk:
                uri = Files.getContentUri(volumeName);
                break;
            case Music:
                uri = Audio.Media.getContentUri(volumeName);
                break;
            case Video:
                uri = MediaStore.Video.Media.getContentUri(volumeName);
                break;
            case Picture:
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;
            default:
                uri = null;
        }
        return uri;
    }

    /**
     * InputStream to byte
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();

        return data;
    }

    /**
     * Byte to bitmap
     *
     * @param bytes
     * @param opts
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes,
                                            BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }

        return null;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context ctx) {
        String state = Environment.getExternalStorageState();
        long blockSize = 0;
        long totalBlocks = 0;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (getStoragePath(ctx).size() > 1) {
                String sdPath = getStoragePath(ctx).get(1);
                StatFs sf = new StatFs(sdPath);
                blockSize = sf.getBlockSize();
                totalBlocks = sf.getBlockCount();
            }
        }
        return Formatter.formatFileSize(ctx, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context ctx) {
        String state = Environment.getExternalStorageState();
        long blockSize = 0;
        long availableBlocks = 0;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (getStoragePath(ctx).size() > 1) {
                String sdPath = getStoragePath(ctx).get(1);
                StatFs sf = new StatFs(sdPath);
                blockSize = sf.getBlockSize();
                availableBlocks = sf.getAvailableBlocks();
            }
        }

        return Formatter.formatFileSize(ctx, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static long getRomTotalSize(Context ctx) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static long getRomAvailableSize(Context ctx) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * @return List，storage信息.通过映射来获取Sd卡路径
     */

    private static List<String> getStoragePath(Context ctx) {
        List<String> pathList = new ArrayList<>();
        StorageManager storageManager = (StorageManager) ctx
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod(
                    "getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            for (int i = 0; i < ((String[]) invoke).length; i++) {
                pathList.add(((String[]) invoke)[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /**
     * 获取文件父目录
     *
     * @param srcFile
     * @return
     */
    public static String getFileParent(File srcFile) {
        if (srcFile == null) {
            return null;
        }
        if (!srcFile.exists()) {
            return null;
        }
        return srcFile.getParent();
    }


//    public static void openFile(Context context, FileSystemObject fso,
//                                String mimeType) {
//        if (TextUtils.isEmpty(mimeType) || fso == null)
//            return;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(fso.getFileUri(), mimeType);
//        context.startActivity(intent);
//    }
}
