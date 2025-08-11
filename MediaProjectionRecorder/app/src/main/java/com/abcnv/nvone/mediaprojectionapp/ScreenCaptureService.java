package com.abcnv.nvone.mediaprojectionapp;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
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

    public void onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    boolean isRecodering = false;
    MediaProjection mediaProjection;
    MediaRecorder mediaRecorder;
    VirtualDisplay virtualDisplay;

    public void startRecorderScreen(Intent resultData, String path, int width, int height, int dpi) {
        Notification notification = NvMediaProjectionUtil.INSTANCE.getNotification(this, "system0011", "ForegroundService");
        startForeground(1000, notification);

        mediaProjection = NvMediaProjectionUtil.INSTANCE.getMediaProjection(this, Activity.RESULT_OK, resultData);
        mediaRecorder = NvMediaProjectionUtil.INSTANCE.initMediaRecorder(this,  path, width, height);
        if (mediaRecorder == null) {
            isRecodering = false;
        }
        try{
            virtualDisplay = NvMediaProjectionUtil.INSTANCE.initVirtualDisplay(mediaProjection, mediaRecorder.getSurface(), width, height);
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener(){
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    stopRecorderScreen();
                }
            });
            isRecodering = NvMediaProjectionUtil.INSTANCE.startRecorderScreen(mediaRecorder);
        }catch (Exception e){
            e.printStackTrace();
        }
        isRecodering = false;
    }

    public void stopRecorderScreen() {
        if (mediaRecorder != null ) {
            NvMediaProjectionUtil.INSTANCE.stopRecorderScreen(mediaProjection, mediaRecorder,virtualDisplay);
        }
        mediaRecorder = null;
        mediaProjection = null;
        virtualDisplay = null;
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