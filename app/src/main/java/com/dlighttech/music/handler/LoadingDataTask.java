package com.dlighttech.music.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理大量音乐数据的task
 * <p/>
 * Created by zhujiang on 16-7-22.
 */
public class LoadingDataTask<Param> {


    private Thread mThread;
    public static final String THREAD_NAME = "LoadDataThread";
    private static final int LOAD_SUCCESS = 0;
    private static final int LOAD_FAIL = 1;
    private List<Param> mParam =new ArrayList<Param>();
    private OnLoadDataListener mListener;

    public static final int LOAD_DATA = 0;
    public static final int UPDATE_DATA = 1;

    public LoadingDataTask(OnLoadDataListener listener) {
        this.mListener = listener;
    }


    public void doInBackGround() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 让线程睡眠1000毫秒，防止数据量过少，执行过快
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mParam = mListener.onLoadingData();
                if (mParam == null) {
                    Message.obtain(mHandler, LOAD_FAIL).sendToTarget();
                    return;
                }
                Message.obtain(mHandler, LOAD_SUCCESS, mParam).sendToTarget();
            }
        }, THREAD_NAME);

        mThread.start();
    }


    public void removeAllMessage() {
        mHandler.removeMessages(LOAD_SUCCESS);
        mHandler.removeMessages(LOAD_FAIL);
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_SUCCESS:
                    List<Param> mParams = (List<Param>) msg.obj;
                    mListener.onLoadDataSuccess(mParams);
                    break;
                case LOAD_FAIL:
                    mListener.onLoadDataFail("数据加载失败………………");
                    break;
                default:
                    break;
            }
            removeAllMessage();
        }
    };


    public interface OnLoadDataListener<Param> {

        /**
         * this method woking in thread for loading data.
         *
         * @return
         */
        List<Param> onLoadingData();

        /**
         * on data loaded sucessful.
         *
         * @param mParams
         */
        void onLoadDataSuccess(List<Param> mParams);

        /**
         * on data load failed.
         */
        void onLoadDataFail(String msg);
    }


}
