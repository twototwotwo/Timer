package com.wangsijiu.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.os.Bundle;

import android.os.Handler;

import com.wangsijiu.app.R;

public class startActivity extends Activity{

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                Intent intent=new Intent(startActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startface);

        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        };
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}
