package com.wangsijiu.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wangsijiu.app.Data;
import com.wangsijiu.app.KeepLiveService;
import com.wangsijiu.app.R;
import com.wangsijiu.app.Utils;
import com.wangsijiu.app.layout.schoolLayout;
import com.wangsijiu.app.layout.selfLayout;
import com.wangsijiu.app.layout.workLayout;

public class mainActivity extends Activity{
    private long currentTime=10000;
    private long lastTime=0;
    LinearLayout mainLayout;
    LinearLayout face;
    LinearLayout top_navigation;
    LinearLayout bottom_navigation;
    selfLayout self;
    RelativeLayout work;
    LinearLayout school;
    Point size;
    ImageButton[] controlButton=new ImageButton[4];//底部导航栏的4个按钮
    int function=1;//0,1,2，3表示三个功能主界面
    private  NotificationManager notificationManager;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            currentTime=System.currentTimeMillis();
            Log.d("time", String.valueOf(currentTime));
            if(currentTime-lastTime<1000) {
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "再点一次退出", Toast.LENGTH_SHORT).show();
                lastTime=currentTime;
            }
            return true;
        }else return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparent(0);//设置状态栏透明
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        size=new Point();
        wm.getDefaultDisplay().getSize(size);
        setContentView(R.layout.activity_mainface);

        work=new workLayout(this,size,getIntent().getStringExtra("data"));
        self=new selfLayout(this,size,getIntent().getStringExtra("data"));
        school=new schoolLayout(this,size);
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        bottom_navigation=(LinearLayout)findViewById(R.id.bottom_navigation);
        top_navigation=(LinearLayout)findViewById(R.id.top_navigation);
        this.setMonitor();
        //开启后台闹钟服务
        /*Intent intent=new Intent(this, KeepLiveService.class);
        startService(intent);*/

        //建立通知渠道
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=26){
            NotificationChannel channel=new NotificationChannel("work","作业通知渠道",NotificationManager.IMPORTANCE_HIGH);
            channel.setBypassDnd(true);
            channel.setLockscreenVisibility(100);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }
        //检查权限
        if(!Data.isRequestNotfication){
            Utils.requestNotification(mainActivity.this);
        }

    }
    public void setMonitor(){
        controlButton[0]=(ImageButton)findViewById(R.id.question);
        controlButton[1]=(ImageButton)findViewById(R.id.work);
        controlButton[2]=(ImageButton)findViewById(R.id.user);
        controlButton[0].setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (function!=0) {
                    controlButton[0].setImageResource(R.drawable.school_press);
                    function = 0;
                    convertFace();
                }
            }
        });
        controlButton[1].setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (function != 1) {
                    controlButton[1].setImageResource(R.drawable.work_press);
                    function = 1;
                    convertFace();
                }
            }
        });
        controlButton[2].setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (function!=2) {
                    controlButton[2].setImageResource(R.drawable.self_press);
                    function = 2;
                    convertFace();
                }
            }
        });
        convertFace();
    }
    public void convertFace(){
        if(function==2){
            controlButton[0].setImageResource(R.drawable.school_unpress);
            controlButton[1].setImageResource(R.drawable.work_unpress);
            mainLayout.removeAllViews();
            mainLayout.addView(top_navigation);
            mainLayout.addView(self);
            mainLayout.addView(bottom_navigation);
            mainLayout.invalidate();
        }else if (function==1){
            controlButton[0].setImageResource(R.drawable.school_unpress);
            controlButton[2].setImageResource(R.drawable.self_unpress);
            mainLayout.removeAllViews();
            mainLayout.addView(top_navigation);
            mainLayout.addView(work);
            mainLayout.addView(bottom_navigation);
            mainLayout.invalidate();
        }else if(function==0){
            controlButton[1].setImageResource(R.drawable.work_unpress);
            controlButton[2].setImageResource(R.drawable.self_unpress);
            mainLayout.removeAllViews();
            mainLayout.addView(top_navigation);
            mainLayout.addView(school);
            mainLayout.addView(bottom_navigation);
            mainLayout.invalidate();
        }
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



    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
        self.updataInfo();

        }




}
