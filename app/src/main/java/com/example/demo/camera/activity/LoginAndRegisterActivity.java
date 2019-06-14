package com.example.demo.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.camera.R;
import com.example.demo.camera.utils.Database;

/**
 * 登录与注册页面
 */
public class LoginAndRegisterActivity extends BaseActivity {

    private LinearLayout registContainer,loginContainer;
    private EditText registEmail,registPass,registVPass,loginEmail,loginPass,idEdit;
    private TextView switchView;
    private Button regist,login;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        getComponent();

        initUi();

        initToolbar();

        click();
    }

    private void getComponent(){

        registContainer= (LinearLayout) findViewById(R.id.regist_container);
        loginContainer= (LinearLayout) findViewById(R.id.login_container);
        registEmail= (EditText) findViewById(R.id.edit_email);
        registPass= (EditText) findViewById(R.id.edit_pass);
        registVPass= (EditText) findViewById(R.id.edit_v_pass);
        loginEmail= (EditText) findViewById(R.id.login_email);
        loginPass= (EditText) findViewById(R.id.login_pass);
        switchView= (TextView) findViewById(R.id.switch_text);
        regist= (Button) findViewById(R.id.btn_regist);
        login= (Button) findViewById(R.id.btn_login);
        idEdit= (EditText) findViewById(R.id.edit_name);
        toolbar= (Toolbar) findViewById(R.id.l_toolbar);

    }

    private void initToolbar(){

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("登录&注册");

        setSupportActionBar(toolbar);



    }


    private void initUi(){

        Intent intent=getIntent();
        if (intent!=null){

            int type=intent.getIntExtra("type",-1);

            if (type!=-1){

                switch(type){

                    case 10:

                        registContainer.setVisibility(View.GONE);
                        loginContainer.setVisibility(View.VISIBLE);

                        break;

                    case 20:

                        registContainer.setVisibility(View.VISIBLE);
                        loginContainer.setVisibility(View.GONE);
                        break;


                }


            }


        }


    }

    private void click(){


        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView();
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void login(){

        String email=loginEmail.getText().toString();
        if (TextUtils.isEmpty(email)){

            Toast.makeText(this, "请输入登录信息", Toast.LENGTH_SHORT).show();

            return;
        }

        String pass=loginPass.getText().toString();

        if (TextUtils.isEmpty(pass)){

            return;
        }

        int s= Database.getInstance(this).login(email,pass);

        if (s>0){
            openMain();

        }else {

            Toast.makeText(this, "密码错误或用户不存在", Toast.LENGTH_SHORT).show();
        }



        //登录操作


    }

    private void regist(){

        String email=registEmail.getText().toString();
        String pass=registPass.getText().toString();
        String pass_v=registVPass.getText().toString();
        String name=idEdit.getText().toString();

        if (TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(pass_v)||TextUtils.isEmpty(name)){

            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();

            return;

        }

        if (!pass.equals(pass_v)){


            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        //注册操作

        int s= Database.getInstance(this).saveUser(email,pass);

        if (s>0){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();

            switchView();

        }else {

            Toast.makeText(this, "注册失败，该用户已存在", Toast.LENGTH_SHORT).show();
        }




    }

    private void switchView(){

        if (registContainer.getVisibility()== View.VISIBLE){

            registContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);

        }else {

            registContainer.setVisibility(View.VISIBLE);
            loginContainer.setVisibility(View.GONE);

        }

    }

    private void openMain(){

        Intent intent=new Intent(this,MainActivity.class);

        startActivity(intent);

        finish();

    }


}
