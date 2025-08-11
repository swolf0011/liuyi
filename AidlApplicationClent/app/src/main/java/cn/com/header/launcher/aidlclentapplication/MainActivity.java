package cn.com.header.launcher.aidlclentapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import cn.com.header.launcher.aidlserviceapp.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    IMyAidlInterface aidl;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidl = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidl = null;
        }
    };
    AppCompatButton button;
    AppCompatTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setPackage("cn.com.header.launcher.aidlserviceapp");
        intent.setAction("cn.com.header.launcher.aidlserviceapp.MyService");
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = aidl.getInfo("20180223");
                    textView.setText(str);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
