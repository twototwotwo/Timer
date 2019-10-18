package com.wangsijiu.app;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/14.
 */
public class work {
    public String subject;
    public Date endDate;
    public Date alarmDate;
    public  String time;
    public String alarmTime;
    public String content;
    public work(String subject,String time,String content){
        this.subject=subject;
        this.time=time;
        this.content=content;
    }

    public work(String subject,String time,String content,Date endDate){
        this.subject=subject;
        this.time=time;
        this.content=content;
        this.endDate=endDate;

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c=Calendar.getInstance();
        try {
            this.endDate=dateFormat.parse(this.time);
            c.setTime(endDate);
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            this.alarmTime=dateFormat.format(c.getTime());
            this.alarmDate=c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public work(String data){
        data=data.trim();
        String[] split_data=data.split("&&");
        if (split_data.length!=3){
            Log.d("dataError",data);
        }

        this.subject=split_data[0];
        this.content=split_data[1];
        this.time=split_data[2];
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c=Calendar.getInstance();
        try {
            this.endDate=dateFormat.parse(this.time);
            c.setTime(endDate);
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            this.alarmTime=dateFormat.format(c.getTime());
            this.alarmDate=c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public String toString() {
        return subject+"&&"+content+"&&"+time;
    }
}
