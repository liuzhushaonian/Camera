package com.example.demo.camera.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 基础Activity
 */
public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("ss", Context.MODE_PRIVATE);
    }
}
