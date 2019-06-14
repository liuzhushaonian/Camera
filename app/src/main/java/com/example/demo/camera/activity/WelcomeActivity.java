package com.example.demo.camera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.Toast;

import com.example.demo.camera.R;


/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    String[] permissionString = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initCamera();


    }

    /**
     * 倒计时，2s后进入下一个页面
     */
    private void startCountdown(){

        new Thread(){
            @Override
            public void run() {
                super.run();

                int i=2;

                while (i>=0){

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message message=new Message();
                    message.what=20;
                    message.arg1=i;
                    handler.sendMessage(message);


                    i--;
                }

            }
        }.start();

    }

    /**
     * 进入下一个页面代码
     */
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 20:

                    int s=msg.arg1;

                    if (s==0){

                        Intent intent=new Intent(WelcomeActivity.this,LoginAndRegisterActivity.class);

                        startActivity(intent);

                        finish();

                    }

                    break;
            }

        }
    };

    /**
     * 检测相机权限
     */
    private void initCamera() {

        if (ContextCompat.checkSelfPermission(this,permissionString[0])!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
        }else {

            startCountdown();

        }

    }




    /**
     * 获取权限后的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 100:

                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED) {//申请成功

                    startCountdown();

                } else {//申请失败
                    Toast.makeText(this,"无法获取相机权限",Toast.LENGTH_SHORT).show();
                }

                break;


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }

    }


}
