package com.wangsijiu.app.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.wangsijiu.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeedTimeActivity extends Activity {
    private long currentTime=10000;
    private long lastTime=0;
    private ImageView treeView;
    private Button startButton;
    private TimePickerView tpView;
    private TextView infoView;
    private ImageView backButton;
    private  Point size;
    private boolean isRun=false;
    Date endDate;
    private int[] treeId={R.drawable.tree0,R.drawable.tree1,R.drawable.tree2,
            R.drawable.tree4,R.drawable.tree4,R.drawable.tree5,
            R.drawable.tree6,R.drawable.tree7,R.drawable.tree8,
            R.drawable.tree9,R.drawable.tree10,R.drawable.tree11};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==12){
                infoView.setText("因为你的专心，种子成功的长成了一棵树");
                infoView.setVisibility(View.VISIBLE);
            }else {
                treeView.setImageResource(treeId[msg.what]);
                treeView.invalidate();
            }

        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            currentTime=System.currentTimeMillis();
            Log.d("time",String.valueOf(currentTime));
            if(currentTime-lastTime<1000) {
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"再点一次退出",Toast.LENGTH_SHORT).show();
                lastTime=currentTime;
            }
            return true;
        }else return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparent(0);
        setContentView(R.layout.activity_time_seed);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        size=new Point();
        wm.getDefaultDisplay().getSize(size);

        treeView=(ImageView)findViewById(R.id.tree);
        treeView.setImageResource(R.drawable.tree0);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, size.x/2*3);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        treeView.setLayoutParams(params);


        startButton=(Button)findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpView.show();
            }
        });

        tpView=new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                endDate=date;
                startSeed();

            }
        }).setType(new boolean[]{false, false, true, true, true, false})
                .setTitleText("选择结束时间")
                .build();

        infoView=(TextView)findViewById(R.id.info);

        backButton=(ImageView)findViewById(R.id.back3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRun){
            isRun=false;
            Toast.makeText(this,"很遗憾，你没能完成挑战",Toast.LENGTH_SHORT).show();
            infoView.setText("很遗憾，你没能完成挑战！！！");
            infoView.setVisibility(View.VISIBLE);
        }
    }

    public boolean startSeed(){
        Date currentDate=new Date(System.currentTimeMillis());
        if(currentDate.getTime()>endDate.getTime()){
            Toast.makeText(this,"时间选择错误，无法开启！",Toast.LENGTH_SHORT).show();
           return false;
        }else{
            Toast.makeText(this,"请专心学习，让种子慢慢长成树。",Toast.LENGTH_SHORT).show();
        }
        startButton.setVisibility(View.GONE);
        treeView.setVisibility(View.VISIBLE);

        final Long time=endDate.getTime()-currentDate.getTime();
        //Log.d("time",String.valueOf(time));
        isRun=true;
        new Thread(){
            @Override
            public void run() {
                int i=0;

                while(isRun){
                    try {
                        Thread.sleep(time/12);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg=new Message();
                    if(i<12){
                        msg.what=i;
                        handler.sendMessage(msg);
                        i+=1;
                    }else {
                        msg.what=12;
                        handler.sendMessage(msg);
                        isRun=false;
                    }
                    //Log.d("tree","tree");
                }
            }
        }.start();
        return true;
    }


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
