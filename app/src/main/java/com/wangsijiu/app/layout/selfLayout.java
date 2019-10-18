package com.wangsijiu.app.layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsijiu.app.Data;
import com.wangsijiu.app.ImgTextView;
import com.wangsijiu.app.KeepLiveService;
import com.wangsijiu.app.User;
import com.wangsijiu.app.R;
import com.wangsijiu.app.activity.SeedTimeActivity;
import com.wangsijiu.app.activity.SelfInfoActivity;
import com.wangsijiu.app.activity.YibanInfoActivity;

/**
 * Created by Administrator on 2019/6/15.
 */
public class selfLayout extends LinearLayout {
    User user;
    FrameLayout personalInfo;
    ImageView head;
    ImgTextView settel;
    ImgTextView function;
    ImgTextView selfInfo;
    ImgTextView yibanInfo;

    TextView ID;
    TextView stuID;
    Point size;
    int height;
    Context context;
    public selfLayout(Context context,Point size,String data) {
        super(context);
        this.context=context;
        this.size=size;
        this.height=size.y/15;
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,0);
        params.weight=12;
        params.gravity=Gravity.CENTER_VERTICAL;
        this.setOrientation(VERTICAL);
        this.setLayoutParams(params);
        this.setBackgroundColor(getResources().getColor(R.color.APPBcolor1));

        user=new User(context.getSharedPreferences("user",Context.MODE_PRIVATE));
        this.init();
    }



    public void init(){
      personalInfo=new FrameLayout(getContext());
        personalInfo.setBackgroundColor(getResources().getColor(R.color.APPBcolor2));
        LayoutParams Pparams=new LayoutParams(LayoutParams.MATCH_PARENT,3*height);
        Pparams.gravity=Gravity.TOP;
        personalInfo.setLayoutParams(Pparams);
        this.addView(personalInfo);

        CardView headCv=new CardView(getContext());
        FrameLayout.LayoutParams headParams=new   FrameLayout.LayoutParams(2*height,2*height);
        headParams.gravity=Gravity.CENTER_VERTICAL|Gravity.LEFT;
        headParams.setMargins(height / 2, height / 2, height / 2, height / 2);
        headCv.setLayoutParams(headParams);
        head=new ImageView(getContext());
        head.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (Data.head!=null){
            head.setImageBitmap(Data.head);
        }else {
            head.setImageResource(R.drawable.head);
        }

        headCv.addView(head);
        headCv.setRadius(30f);
        personalInfo.addView(headCv);

        ID=new TextView(getContext()) ;
        ID.setText(user.ID);
        ID.setGravity(Gravity.CENTER_VERTICAL);
        ID.setTextSize(24);
        FrameLayout.LayoutParams textParams=new   FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,height);
        textParams.setMargins(3 * height, height / 2, 0, 0);
        ID.setLayoutParams(textParams);
        personalInfo.addView(ID);

        stuID=new TextView(getContext()) ;
        stuID.setAlpha(0.5f);
        stuID.setText("学号:" + user.stuID);
        stuID.setGravity(Gravity.CENTER_VERTICAL);
        stuID.setTextSize(16);
        FrameLayout.LayoutParams stuIDParams=new   FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,height);
        stuIDParams.setMargins(3 * height, height / 2 + height, 0, 0);
        stuID.setLayoutParams(stuIDParams);
        personalInfo.addView(stuID);

      LayoutParams params;
      function=new ImgTextView(getContext(),R.drawable.seed,"时间种子",size);
        params=new LayoutParams(LayoutParams.MATCH_PARENT,height);
        params.setMargins(0, 5, 0, 5);
        function.setLayoutParams(params);
        function.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeedTimeActivity.class);
                getContext().startActivity(intent);
            }
        });
        this.addView(function);

      selfInfo=new ImgTextView(getContext(),R.drawable.selfinfo,"个人信息",size);
        params=new LayoutParams(LayoutParams.MATCH_PARENT,height);
        params.gravity=Gravity.BOTTOM;
        params.setMargins(0, 5, 0, 5);
        selfInfo.setLayoutParams(params);
        this.addView(selfInfo);

      yibanInfo=new ImgTextView(getContext(),R.drawable.yiban,"易班信息",size);
        params=new LayoutParams(LayoutParams.MATCH_PARENT,height);
        params.gravity=Gravity.BOTTOM;
        params.setMargins(0, 5, 0, 5);
        yibanInfo.setLayoutParams(params);
        this.addView(yibanInfo);

      settel=new ImgTextView(getContext(),R.drawable.settle,"清除授权信息",size);
        params=new LayoutParams(LayoutParams.MATCH_PARENT,height);
        params.gravity=Gravity.BOTTOM;
        params.setMargins(0, 35, 0, 5);
        settel.setLayoutParams(params);
        this.addView(settel);

      this.setListener();
    }

   public void updataInfo(){
       //更新面板的个人信息
       Log.d("update", "updataInfo:ss ");
       SharedPreferences sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
       String stuID=sharedPreferences.getString("stuID","");
       String ID=sharedPreferences.getString("ID","");
       this.stuID.setText(stuID);
       this.ID.setText(ID);
       this.invalidate();

       if (Data.head!=null){
           head.setImageBitmap(Data.head);
       }
   }

    public void setListener(){
        selfInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelfInfoActivity.class);
                getContext().startActivity(intent);
                Intent intent1=new Intent(getContext(),KeepLiveService.class);
                getContext().stopService(intent1);

            }
        });

        yibanInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), YibanInfoActivity.class);
                getContext().startActivity(intent);
            }
        });

        settel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();

                editor.commit();
                Toast.makeText(context,"授权信息已清除",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
