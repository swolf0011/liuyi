package cn.com.header.launcher.aidlserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
    public MyService() {
    }
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }


    private IBinder mIBinder = new IMyAidlInterface.Stub() {
        @Override
        public String getInfo(String id) throws RemoteException {
            return id+" to info LiuYi";
        }
    };
}
