package com.wangsijiu.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/13.
 */


    public class workView extends RelativeLayout{
        TextView subjectView;
        TextView timeView;
        TextView alarmTimeView;
        ImageButton switchButton;
        TextView contentView;
        ImageView openButton;
        work work;
        int height;
        int width;
        int position;
        boolean isOpen=false;
        boolean isOn_timer=false;
        public workView(Context context,Point size) {
            super(context);
            this.height=size.y/15;
            this.width=size.x;
            this.setBackgroundResource(R.drawable.workview_shape);
            this.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2 * height));
        }

        public void init(work work,int position){
            this.position=position;
            this.work=work;
            subjectView=new TextView(getContext());
            LayoutParams params=new LayoutParams(width/3,height);
            params.addRule(ALIGN_PARENT_LEFT);
            subjectView.setLayoutParams(params);
            subjectView.setPadding(5, 0, 0, 0);
            subjectView.setTypeface(Typeface.DEFAULT_BOLD);
            subjectView.setText(work.subject);
            subjectView.setTextSize(18);
            //subjectView.setGravity(Gravity.CENTER_VERTICAL);
            this.addView(subjectView);

            timeView=new TextView(getContext());
            LayoutParams params1=new LayoutParams(2*width/3,height/2);
            params1.addRule(ALIGN_PARENT_TOP | ALIGN_PARENT_RIGHT);
            timeView.setTextSize(12);
            timeView.setLayoutParams(params1);
            timeView.setTypeface(Typeface.DEFAULT_BOLD);
            timeView.setText("截止:" + work.time);
            timeView.setGravity(Gravity.CENTER_VERTICAL);
            this.addView(timeView);

            alarmTimeView=new TextView(getContext());
            LayoutParams params5=new LayoutParams(2*width/3,height/2);
            params5.addRule(ALIGN_PARENT_TOP | ALIGN_PARENT_RIGHT);
            params5.setMargins(0, height / 2, 0, 0);
            alarmTimeView.setTextSize(12);
            alarmTimeView.setLayoutParams(params5);
            alarmTimeView.setTypeface(Typeface.MONOSPACE);
            alarmTimeView.setText("定时:" +"无");
            alarmTimeView.setGravity(Gravity.CENTER_VERTICAL);
            this.addView(alarmTimeView);

            switchButton=new ImageButton(getContext());
            LayoutParams params6=new LayoutParams(height/3*2,height/3*2);
            params6.addRule(ALIGN_PARENT_TOP | ALIGN_PARENT_RIGHT);
            switchButton.setLayoutParams(params6);
            if (isOn_timer) switchButton.setBackgroundResource(R.drawable.timer_on);
            else switchButton.setBackgroundResource(R.drawable.timer_off);
            switchButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            this.addView(switchButton);





            contentView=new TextView(getContext());
            RelativeLayout.LayoutParams params2=new LayoutParams(width,height);
            params2.addRule(ALIGN_PARENT_BOTTOM);
            contentView.setLayoutParams(params2);
            contentView.setText(work.content);
            contentView.setPadding(10,0,10,0);
            this.addView(contentView);


            View line=new View(getContext());
            LayoutParams params3=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
            params3.setMargins(0, height, 0, 0);
            line.setLayoutParams(params3);
            line.setBackgroundColor(Color.BLACK);
            this.addView(line);

//            openButton=new ImageView(getContext());
//            LayoutParams params4=new LayoutParams(height,height);
//            params4.addRule(ALIGN_PARENT_BOTTOM);
//            params4.addRule(ALIGN_PARENT_RIGHT);
//           // params4.setMargins(0, 0, height / 4, height / 4);
//            openButton.setLayoutParams(params4);
//            openButton.setImageResource(R.drawable.open);
//            this.addView(openButton);

        this.setListener();
        }

        public void setListener(){
            this.contentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    RelativeLayout.LayoutParams params1 =
                            (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                    if (!isOpen) {
                        params.height = 6 * height;
                        params1.height = 5 * height;

                        //openButton.setImageResource(R.drawable.shrink);
                        setLayoutParams(params);
                        contentView.setLayoutParams(params1);
                        isOpen = true;


                    } else {
                        params.height = 2 * height;
                        params1.height = height;
                        isOpen = false;

                        //openButton.setImageResource(R.drawable.open);
                        setLayoutParams(params);
                        contentView.setLayoutParams(params1);


                    }
                }
            });

            final TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //SharedPreferences sP=getContext().getSharedPreferences("work",Context.MODE_PRIVATE);
                    Intent intent=new Intent("com.workAPP.Broad");
                    intent.putExtra("position",position);
                    intent.putExtra("boolean",true);
                    intent.putExtra("alarmTime",date.getTime());
                    intent.putExtra("subject",work.subject);
                    getContext().sendBroadcast(intent);

                    work.alarmDate=date;
                    alarmTimeView.setText("定时:" + formatter.format(date));
                    alarmTimeView.invalidate();

                    isOn_timer = true;
                    switchButton.setBackgroundResource(R.drawable.timer_on);
                }


            })      .setType(new boolean[]{true, true, true, true, true, true})
                    .setTitleText("设置提醒时间")
                    .build();

            switchButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOn_timer) {
                        isOn_timer = false;
                        switchButton.setBackgroundResource(R.drawable.timer_off);
                        Intent intent=new Intent("com.workAPP.Broad");
                        intent.putExtra("position",position);
                        intent.putExtra("boolean",false);
                        getContext().sendBroadcast(intent);
                        alarmTimeView.setText("定时:" +"无");
                        alarmTimeView.invalidate();
                    } else {
                        //检查通知权限
                        Utils.checkNotifySetting(getContext());
                        //检查通知服务是否启动,并启动
                        Utils.checkService(getContext());
                        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                        pvTime.show();

                    }
                }
            });
        }


}

