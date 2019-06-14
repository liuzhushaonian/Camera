package com.example.demo.camera.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.demo.camera.R;


/**
 * 真正显示设置的地方
 */
public class SettingFragment extends PreferenceFragment {


    private EditTextPreference editTextPreference;
    private PreferenceScreen preferenceScreen;
    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences("ss", Context.MODE_PRIVATE);//初始化sharedPreferences

        addPreferencesFromResource(R.xml.preferences);

        editTextPreference= (EditTextPreference) findPreference("local");//获取设置的控件
        preferenceScreen= (PreferenceScreen) findPreference("update");

        initLocal();
        initUpdate();


    }



    /**
     * 更改路径
     */
    private void initLocal(){



        String s=sharedPreferences.getString("path","/pic/");//获取路径

        editTextPreference.setSummary(s);//显示路径

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {


                sharedPreferences.edit().putString("path", (String) o).apply();//保存路径

//                editTextPreference.setSummary((String)o);

                return true;
            }
        });


    }

    /**
     * 检测更新
     * 伪代码
     */
    private void initUpdate(){

        preferenceScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "当前为最新版本", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }



}
