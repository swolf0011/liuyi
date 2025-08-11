package com.abcnv.nvone.mediaprojectionapp;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.IBinder;

import com.abcnv.nvone.mediaprojectionapp.util.NvMediaProjectionUtil;

public class ScreenCaptureService extends Service {
    public ScreenCaptureService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ScreenCaptureServiceBinder();
    }


    class ScreenCaptureServiceBinder extends Binder {
        public ScreenCaptureService getService() {
            return ScreenCaptureService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }
    public void startCapture(Intent resultData,int width,int height,ICallback callback){
        Notification notification = NvMediaProjectionUtil.INSTANCE.getNotification(this,"system0011","ForegroundService") ;
        startForeground(1000, notification);

        MediaProjection mediaProjection = NvMediaProjectionUtil.INSTANCE.getMediaProjection(this, Activity.RESULT_OK,resultData);
        Bitmap bitmap = NvMediaProjectionUtil.INSTANCE.startCapture(mediaProjection,width, height);
        callback.handler(bitmap);
    }
    interface ICallback{
        void handler(Bitmap bitmap);
    }
}