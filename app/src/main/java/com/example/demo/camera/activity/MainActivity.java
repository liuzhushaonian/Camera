package com.example.demo.camera.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.camera.R;
import com.example.demo.camera.camera.CameraFaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 主要用来拍照的页面
 */
public class MainActivity extends BaseActivity implements SurfaceHolder.Callback{

    FrameLayout frameLayout;
    CameraFaceView cameraFaceView;
    String[] permissionString = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};

    ImageView take;
    private Camera mCamera;//摄像头实例
    private int camera_id;//摄像头id，区别是哪个摄像头
    private FrameLayout s3,s5,s10;
    private ImageView more;

    private boolean isTimeMode=false;//定时模式

    private TextView countDown;

    private boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        checkSd();

        getComponent();

        init();

        click();
    }

    private long exitTime=0;

    //按两次返回键退出
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        exit();

    }

    //计算两次返回键时间以及退出
    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
                finish();
                System.exit(0);
            }

    }

    /**
     * 获取各个控件的实例
     */
    private void getComponent() {

        frameLayout = (FrameLayout) findViewById(R.id.frame_view);
        take= (ImageView) findViewById(R.id.take_button);
        cameraFaceView= (CameraFaceView) findViewById(R.id.camera_view);
        s3= (FrameLayout) findViewById(R.id.btn_3);
        s5= (FrameLayout) findViewById(R.id.btn_5);
        s10= (FrameLayout) findViewById(R.id.btn_10);
        countDown= (TextView) findViewById(R.id.count_down);
        more= (ImageView) findViewById(R.id.setting);

    }

    /**
     * 初始化相机，在这之前必须先获取相机权限
     */
    private void init(){

        mCamera=getCameraInstance();//获取前摄像头

        cameraFaceView.getHolder().addCallback(this);
        cameraFaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraFaceView.setCameraDisplayOrientation(this,this.camera_id,this.mCamera);


    }

    /**
     * 获取前摄像头
     * @return 返回摄像头
     */
    private Camera getCameraInstance(){
        Camera c = null;

        try {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras(); // get cameras number

            for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
                Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
                if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    c = Camera.open(camIdx);
                    this.camera_id=camIdx;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return c;
    }

    /**
     * 各种控件点击事件
     */
    private void click(){

        //拍照点击
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTimeMode){
                    hideTimeButton();
                }else {

                    takePicture();
                }
            }
        });

        //拍照长按
        take.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(MainActivity.this, "切换到定时拍摄", Toast.LENGTH_SHORT).show();

                showTimeButton();

                return true;
            }
        });

        //3秒拍照
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCountDown(3);
                hideTimeButton();


            }
        });

        //5秒拍照
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountDown(5);
                hideTimeButton();
            }
        });

        //10秒拍照
        s10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountDown(10);
                hideTimeButton();
            }
        });

        //更多设置
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);

            }
        });


    }


    /**
     * 检测sd卡权限
     */
    private void checkSd(){

        if (ContextCompat.checkSelfPermission(this,permissionString[1])!= PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        }else {


            check=true;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 200:

                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {//申请成功

                    check=true;

                } else {//申请失败
                    Toast.makeText(this,"无法获取存储权限",Toast.LENGTH_SHORT).show();
                    check=false;
                }


                break;


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }

    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

    }




    /**
     * 创建holder
     * 初始化相机
     * 配置参数
     * 显示预览
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {

            mCamera=getCameraInstance();


            cameraFaceView.setCameraDisplayOrientation(this,this.camera_id,this.mCamera);

//            mCamera.setDisplayOrientation(270);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            Log.d("sa", "Error setting camera preview: " + e.getMessage());
        }
    }

    /**
     * 界面改变
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        if (cameraFaceView.getHolder().getSurface() == null){

            return;
        }


        try {

            mCamera.stopPreview();
        } catch (Exception e){

        }


        try {
            mCamera.setPreviewDisplay(cameraFaceView.getHolder());
            mCamera.startPreview();
        } catch (Exception e){
            Log.d("preview", "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 界面销毁
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }

    }

    /**
     * 检测摄像头是否存在，这里指前摄像头
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 拍照
     */
    public void takePicture(){

        if (mCamera!=null){

            mCamera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    Log.d("take--->>>","拍照");

                }
            }, null, pictureCallback);

        }

    }

    /**
     * 保存照片
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        /**
         * 拍照完成，可以拿到数据了
         * @param data 数据
         * @param camera 摄像机
         */
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if (!check){
                Toast.makeText(MainActivity.this, "未获得权限，无法保存照片", Toast.LENGTH_SHORT).show();

                return;
            }


            // 写在指定目录 sdcard 中去

            long name=System.currentTimeMillis();

            String path=sharedPreferences.getString("path","/pic/");

            String p=Environment.getExternalStorageDirectory()+path;

            File file = new File(p, name+".jpg");

            if (!file.getParentFile().exists()){//创建父类文件夹，避免更改路径后无法保存图片

                file.getParentFile().mkdirs();

            }


            try {
                // 字节文件输出流，把byte[]数据写入到文件里面去
                OutputStream os = new FileOutputStream(file);
                os.write(data);

                // 关闭字节文件输出流
                os.close();

                mCamera.startPreview();

                Toast.makeText(MainActivity.this, name+".jpg 已保存在sd下", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ss", "保存失败");
                Toast.makeText(MainActivity.this, "照片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    };



    /**
     * 显示三个定时按钮
     */
    private void showTimeButton(){

        int width=getResources().getDisplayMetrics().heightPixels;//获取屏幕高度

        int pd=getResources().getDimensionPixelSize(R.dimen.pd);

        int position=width-pd;

        if (s3.getVisibility()==View.GONE){
            s3.setVisibility(View.VISIBLE);
        }

        if (s5.getVisibility()==View.GONE){
            s5.setVisibility(View.VISIBLE);
        }

        if (s10.getVisibility()==View.GONE){
            s10.setVisibility(View.VISIBLE);
        }


        //动画效果
        ObjectAnimator animator = ObjectAnimator.ofFloat(s3, "translationY", 0, -1.2f*pd).setDuration(200);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(s5, "translationY", 0, -2.4f*pd).setDuration(200);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(s10, "translationY", 0, -3.6f*pd).setDuration(200);

        animator.start();
        animator2.start();
        animator3.start();

        isTimeMode=true;//打开定时模式，并不允许正常拍照

    }

    /**
     * 收回这些定时按钮，开始定时或直接收起
     */
    private void hideTimeButton(){

        int pd=getResources().getDimensionPixelSize(R.dimen.pd);

        ObjectAnimator animator = ObjectAnimator.ofFloat(s3, "translationY", 0, 1.2f*pd).setDuration(200);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(s5, "translationY", 0, 2.4f*pd).setDuration(200);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(s10, "translationY", 0, 3.6f*pd).setDuration(200);

        animator.start();
        animator2.start();
        animator3.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (s3.getVisibility()==View.VISIBLE){
                    s3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (s5.getVisibility()==View.VISIBLE){
                    s5.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (s10.getVisibility()==View.VISIBLE){
                    s10.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        isTimeMode=false;

    }

    /**
     * 倒计时，每过1s发送一次
     * @param time 设置时长，单位秒
     */
    private void startCountDown(final int time){



        new Thread(){
            @Override
            public void run() {
                super.run();

                try {

                    int t=time;

                    while (t>=0) {//每隔1s发送一次

                        Message message = new Message();
                        message.what = 10;
                        message.arg1 = t;
                        message.arg2=time;
                        handler.sendMessage(message);

                        sleep(1000);

                        t--;


                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();



    }


    /**
     * 用于在主线程内更新倒计时，并在倒计时结束的时候拍照
     */
    Handler handler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 10:
                    if (countDown.getVisibility()==View.GONE) {
                        countDown.setVisibility(View.VISIBLE);
                    }

                    int t=msg.arg1;

                    int t2=msg.arg2;

                    String s=t+" s";

                    countDown.setText(s);

                    switch (t2){

                        case 3:

                            countDown.setTextColor(getResources().getColor(R.color.colorAccent));
                            break;

                        case 5:
                            countDown.setTextColor(getResources().getColor(R.color.colorSelect));
                            break;

                        case 10:

                            countDown.setTextColor(getResources().getColor(R.color.colorDeepPurple));
                            break;

                    }

                    if (t==0){//倒计时结束

                        takePicture();//拍照

                        countDown.setVisibility(View.GONE);

                    }

                    break;


            }

        }
    };


}
