package com.example.demo.camera.activity;


import android.app.FragmentTransaction;
import android.os.Bundle;


import com.example.demo.camera.R;


/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addFragment();
    }

    //将设置页面的内容添加上来
    private void addFragment(){

        FragmentTransaction transaction=getFragmentManager().beginTransaction();

        SettingFragment fragment=new SettingFragment();

        transaction.replace(R.id.container,fragment);

        transaction.commit();

    }


}
