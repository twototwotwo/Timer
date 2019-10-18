package com.wangsijiu.app;

import android.content.SharedPreferences;
import android.util.Log;

import com.orm.dsl.Unique;

/**
 * Created by Administrator on 2019/7/3.
 */
public class User{
    @Unique
    public String ID;
    public String stuID;
    public String school;
    public String sex;

    public User(SharedPreferences sharedPreferences){
        ID=sharedPreferences.getString("ID","");
        school=sharedPreferences.getString("school","");
        sex=sharedPreferences.getString("sex","");
        stuID=sharedPreferences.getString("stuID","");
        Log.d("info",sharedPreferences.getString("info",""));
    }

    public User(String ID,String stuID,String school,String sex){
        this.ID=ID;
        this.stuID=stuID;
        this.school=school;
        this.sex=sex;

    }

    public void setInfo(String ID,String stuID,String school,String sex){
        this.ID=ID;
        this.stuID=stuID;
        this.school=school;
        this.sex=sex;

    }

}
