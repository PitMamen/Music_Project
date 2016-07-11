package com.android.app;

import com.dlighttech.music.database.DataBaseManager;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者模式，数据发生改变时，将被调用
 * <p/>
 * Created by zhujiang on 16-7-1.
 */
public class DataChangedWatcher extends Observable {

    private static DataChangedWatcher mWatcher = null;
//    public  boolean isRegister = false;


    private DataChangedWatcher() {

    }

    public static DataChangedWatcher getInstance() {
        if (mWatcher == null) {
            synchronized (DataBaseManager.class) {
                if (mWatcher == null) {
                    mWatcher = new DataChangedWatcher();
                }
            }
        }
        return mWatcher;
    }


    /**
     * 注册观察者
     *
     * @param o
     */
    public void registerObserver(Observer o) {
        addObserver(o);
    }

    /**
     * 反注册观察者
     *
     * @param o
     */
    public void unRegisterObserver(Observer o) {
        deleteObserver(o);
    }

    /**
     * 通知观察者数据已经改变
     *
     * @param data
     */
    public void update(Object data) {
        setChanged();
        notifyObservers(data);
    }

    public void update() {
        setChanged();
        notifyObservers();
    }
}
