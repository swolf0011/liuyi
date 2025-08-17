package com.itfitness.incrementupdatedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.itfitness.incrementupdatedemo.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView tv_version;
    private Button bt_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_version = findViewById(R.id.tv_version);
        bt_update = findViewById(R.id.bt_update);
        tv_version.setText("3.0");
        bt_update.setOnClickListener(v->{
            new Thread(() -> {
                // directory_downloads == "/storage/emulated/0/Android/data/com.itfitness.incrementupdatedemo/files/Download"
                File directory_downloads = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                if(!directory_downloads.exists()){
                    directory_downloads.mkdirs();
                }
                System.out.println("0011 == "+directory_downloads.getAbsolutePath());
                File oldApkFile = new File(directory_downloads, "old.apk");
                File newApkFile = new File(directory_downloads, "new1.apk");
                File patchFile = new File(directory_downloads, "patch");
                if(!oldApkFile.exists() ||!patchFile.exists()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("原包文件或差分文件不存在，请将test_apk下的文件，先放入:\n"+directory_downloads.getAbsolutePath());
                        }
                    });
                    return;
                }
                PatchUtil.patchAPK(oldApkFile.getAbsolutePath(),newApkFile.getAbsolutePath(),patchFile.getAbsolutePath());
                //安装APK
                if (newApkFile.exists()) {
                    AppUtils.installApp(newApkFile);
                }
            }).start();
        });
    }

    public void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}