package com.wangsijiu.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.wangsijiu.app.InfoLayout;
import com.wangsijiu.app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class YibanInfoActivity extends Activity {
    InfoLayout yb_id;
    InfoLayout yb_name;
    InfoLayout yb_money;
    InfoLayout yb_exp;
    InfoLayout yb_school;
    ImageView backButton;
    //TextView changeButton;
    Point size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparent(0);//设置状态栏透明
        setContentView(R.layout.activity_yiban_info);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        size=new Point();
        wm.getDefaultDisplay().getSize(size);

        SharedPreferences sP=getSharedPreferences("user",MODE_PRIVATE);
        String info= sP.getString("info","");
        if(info.equals("")){
            Toast.makeText(getApplicationContext(),"易班信息错误",Toast.LENGTH_LONG).show();
            yb_id=(InfoLayout)findViewById(R.id.yb_id);
            yb_id.init("易班id(唯一)","", size);
            yb_id.setUnFocuse();
            yb_name=(InfoLayout)findViewById(R.id.yb_name);
            yb_name.init("易班实名", "", size);
            yb_name.setUnFocuse();
            yb_money=(InfoLayout)findViewById(R.id.yb_money);
            yb_money.init("易班网薪","", size);
            yb_money.setUnFocuse();
            yb_exp=(InfoLayout)findViewById(R.id.yb_exp);
            yb_exp.init("易班经验", "", size);
            yb_exp.setUnFocuse();
            yb_school=(InfoLayout)findViewById(R.id.yb_school);
            yb_school.init("学校", "", size);
            yb_school.setUnFocuse();
        }
        try {
            JSONObject infoJson=new JSONObject(info);
            yb_id=(InfoLayout)findViewById(R.id.yb_id);
            yb_id.init("易班id(唯一)",infoJson.getString("yb_userid"), size);
            yb_id.setUnFocuse();
            yb_name=(InfoLayout)findViewById(R.id.yb_name);
            yb_name.init("易班实名", infoJson.getString("yb_username"), size);
            yb_name.setUnFocuse();
            yb_money=(InfoLayout)findViewById(R.id.yb_money);
            yb_money.init("易班网薪", infoJson.getString("yb_money"), size);
            yb_money.setUnFocuse();
            yb_exp=(InfoLayout)findViewById(R.id.yb_exp);
            yb_exp.init("易班经验", infoJson.getString("yb_exp"), size);
            yb_exp.setUnFocuse();
            yb_school=(InfoLayout)findViewById(R.id.yb_school);
            yb_school.init("学校", infoJson.getString("yb_schoolname"), size);
            yb_school.setUnFocuse();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        backButton=(ImageView)findViewById(R.id.back2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    //设置状态栏透明，n为透明度
    public void setTransparent(int n){
        //状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
