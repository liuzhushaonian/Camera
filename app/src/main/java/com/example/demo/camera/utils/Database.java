package com.example.demo.camera.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {


    private static volatile Database database;
    private SQLiteDatabase sqLiteDatabase;


    private static final String USER_TABLE="CREATE TABLE IF NOT EXISTS user " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "email TEXT," +
            "pass TEXT" +
            ")";

    public static Database getInstance(Context context){

            if (database==null){

                synchronized (Database.class){

                    database=new Database(context,"camera",null,1);

                }


            }

            return database;

    }


    public Database(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        sqLiteDatabase=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(USER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 注册，如果用户已存在则返回错误信息
     * @param email 邮箱
     * @param pass 密码
     * @return 返回信息，1表示正确，-1表示错误
     */
    public int saveUser(String email,String pass){

        int result=-1;

        String sql="select * from user where email = '"+email+"' limit 1";//查询用户是否已存在

        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);

        if (cursor!=null){

            Log.d("count--->>>",cursor.getCount()+"");

            if (cursor.getCount()>0) {

                cursor.close();

                result = -1;
            }else {

                String sql1="insert into user (email,pass) values ('"+email+"','"+pass+"')";

                sqLiteDatabase.execSQL(sql1);

                result=1;

            }

        }



        return result;

    }

    /**
     * 登录，校验用户是否存在，不存在返回错误，密码错误返回错误
     * @param email 邮箱
     * @param pass 密码
     * @return 1表示正确，-1表示出错
     */
    public int login(String email,String pass){

        int result=-1;

        String sql="select * from user where email = '"+email+"' limit 1";//查询用户是否已存在

        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);

        if (cursor!=null){

            if (cursor.getCount()<=0) {

                cursor.close();

                result = -1;
            }else {


                if (cursor.moveToFirst()){

                    do {

                        String pass1=cursor.getString(cursor.getColumnIndex("pass"));

                        if (pass.equals(pass1)){
                            result=1;
                        }else {

                            result=-1;
                        }



                    }while (cursor.moveToNext());

                }



            }

        }


        return result;

    }

}
