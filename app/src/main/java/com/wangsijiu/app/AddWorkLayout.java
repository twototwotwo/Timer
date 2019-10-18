package com.wangsijiu.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.TimePickerView;
import com.wangsijiu.app.layout.workLayout;

import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/8/26.
 */
public class AddWorkLayout extends RelativeLayout{
    Date endDate=null;
    Point size;
    workLayout parent;
    TimePickerView tpView;
    InfoLayout subjectView;
    InfoLayout timeView;
    InfoLayout contentView;
    public AddWorkLayout(Context context,Point size,workLayout parent){
        super(context);
        this.parent=parent;
        this.size=size;
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setPadding(10,10,10,0);
        this.setLayoutParams(params);
        this.setBackgroundResource(R.color.APPBcolor2);
        this.init();
    }

    public void init(){
         tpView=new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timeView.infoView.setText(formatter.format(date));
                endDate=date;
            }
        }).setType(new boolean[]{true, true, true, true, true, true})
                .setTitleText("选择截止时间")
                .build();


        ImageButton backButton=new ImageButton(getContext());
        LayoutParams params=new LayoutParams(size.y/25,size.y/25);
        backButton.setLayoutParams(params);
        backButton.setBackgroundResource(R.drawable.back);
        backButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
            }
        });
        this.addView(backButton);

        Button sureView=new Button(getContext());
        LayoutParams params1=new LayoutParams(size.y/15, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.addRule(ALIGN_PARENT_RIGHT);
        sureView.setLayoutParams(params1);
        sureView.setText("保存");
        sureView.setBackgroundResource(R.drawable.sure_view_shape);
        //Log.d("size",String.valueOf(getHeight()));
        sureView.setTextSize(size.y/150);
        sureView.setGravity(Gravity.CENTER);
        sureView.setTextColor(Color.BLACK);
       //sureView.setBackgroundResource(R.color.APPBcolor);
        this.addView(sureView);
        sureView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endDate==null){
                    tpView.show();
                }else {
                    work data=new work(subjectView.getInfo(),timeView.getInfo(),contentView.getInfo(),endDate);
                    sendDataRequest(data.toString());
                    parent.addWork(data);
                    timeView.infoView.setText("");
                    subjectView.infoView.setText("");
                    contentView.infoView.setText("");
                    setVisibility(GONE);
                }
            }
        });

        TextView titleView=new TextView(getContext());
        LayoutParams params2=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,size.y/20);
        params2.setMargins(0, size.y / 20, 0, 0);
        params2.addRule(CENTER_HORIZONTAL);
        titleView.setLayoutParams(params2);
        //titleView.setBackgroundColor(Color.BLACK);
        titleView.setText("创建作业");
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(size.y / 80);
        this.addView(titleView);

        subjectView=new InfoLayout(getContext());
        timeView=new InfoLayout(getContext());
        contentView=new InfoLayout(getContext());

        LayoutParams params4=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,size.y/15);
        params4.addRule(CENTER_HORIZONTAL);
        params4.setMargins(size.x / 10, size.y / 5, size.x / 10, 0);
        timeView.setLayoutParams(params4);
        timeView.init(size, "时间:");
        timeView.infoView.setFocusable(false);
        timeView.infoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTpView();
            }
        });
        this.addView(timeView);

        LayoutParams params3=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,size.y/15);
        params3.addRule(CENTER_HORIZONTAL);
        params3.setMargins(size.x / 10, size.y / 10*3, size.x / 10, 0);
        subjectView.setLayoutParams(params3);
        subjectView.init(size, "科目:");
        this.addView(subjectView);

        LayoutParams params5=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,size.y/15);
        params5.addRule(CENTER_HORIZONTAL);
        params5.setMargins(size.x/10, size.y / 10*4, size.x/10, 0);
        contentView.setLayoutParams(params5);
        contentView.init(size,"内容:");
        this.addView(contentView);
    }
    public  void showTpView(){
        tpView.show();
    }

    public void sendDataRequest(final String data) {
        //请求地址
        final String url = "http://116.62.169.215:8080/MyWeb/ReceiverServlet";    //注①
        String tag = "send";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getContext(), "响应错误", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sP=getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
                Map<String,String> map=new HashMap<>();
                map.put("data",data);
                map.put("userid",sP.getString("userid","test"));
                Log.d("userid",sP.getString("userid","test"));
                return map;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        //Log.d("url",request.toString());
        //将请求添加到队列中
        requestQueue.add(request);

    }
}
