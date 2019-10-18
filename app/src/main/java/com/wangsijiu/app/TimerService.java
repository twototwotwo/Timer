package com.wangsijiu.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wangsijiu.app.activity.mainActivity;

import java.util.Date;


public class TimerService extends IntentService {


    public TimerService() {
        super("KeepLiveService");
    }
    boolean isRun=true;
    Long[] alarmTime;
    int size;
    boolean[] isAlarm;
    String[] subject;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("KPservice","creat");
        IntentFilter intentFilter=new IntentFilter("com.workAPP.Broad");
        BroadcastReceiver receiver=new BroadcastReceiver();
        registerReceiver(receiver,intentFilter);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("KeepAppAlive");
        builder.setContentText("DaemonService is runing...");
        startForeground(1, builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("KPservice", "start");
        onStart(intent, startId);

        size=intent.getIntExtra("size",0);
        alarmTime=new Long[1000];
        isAlarm=new boolean[1000];
        subject=new String[1000];
        for(int i=0;i<1000;i++) {
            isAlarm[i] = false;
        }
        //isRun=!isRun;

        return START_NOT_STICKY;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("KPservice", "handle");
        Date nowDate;
        Long nowTime;
        while(isRun){
            //Log.d("alarm","check");
            nowDate=new Date(System.currentTimeMillis());
            nowTime=nowDate.getTime();
            for(int i=0;i<size;i++){
                if (isAlarm[i]){
                    Log.d("alarm",String.valueOf(i));

                    if (nowTime>alarmTime[i]){
                        isAlarm[i]=false;
                        creatNofication(i);
                    }
                }else {
                    continue;
                }

            }

        }


    }

    public void creatNofication(int position){
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        Log.d("version",String.valueOf(Build.VERSION.SDK_INT));
        if(Build.VERSION.SDK_INT>=26){
            Log.d("version",String.valueOf(Build.VERSION.SDK_INT));
            NotificationChannel channel=new NotificationChannel("work","work",NotificationManager.IMPORTANCE_HIGH);
            builder=new NotificationCompat.Builder(this,"work");

        }else {
            builder=new NotificationCompat.Builder(this);

        }
        Intent intent=new Intent(getApplicationContext(),mainActivity.class);
        PendingIntent pIntent=PendingIntent.getActivity(getApplicationContext(),0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentTitle("您有作业即将到期")
                .setContentText("作业:"+subject[position])
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))
                .setSmallIcon(R.drawable.app_icon)
                .setWhen(System.currentTimeMillis())
                .setTicker("作业到期提醒")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pIntent)
                .setAutoCancel(true);


        Log.d("nofication", "nofi");
        notificationManager.notify(position,builder.build());


    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        Log.d("KPservice","exit");
    }


    public class BroadcastReceiver extends android.content.BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int position=intent.getIntExtra("position",0);
            isAlarm[position]=intent.getBooleanExtra("boolean",false);
            if (isAlarm[position]) {
                alarmTime[position]=intent.getLongExtra("alarmTime",0);
                subject[position]=intent.getStringExtra("subject");
                Log.d("alarm","on1");
            }else {
                Log.d("alarm","off");
            }

        }
    }
}
