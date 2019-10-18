package com.wangsijiu.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangsijiu.app.Data;
import com.wangsijiu.app.R;
import com.wangsijiu.app.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

public class LoginActivity extends Activity {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                //跳转到下一个activity
                SharedPreferences sP=getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
                saveSelfInfo(sP.edit());
                Intent intent=new Intent(LoginActivity.this,mainActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
                finish();
            }
            if(msg.what==2){
                Intent intent=new Intent(LoginActivity.this,AuthorizeActivity.class);
                intent.putExtra("url",webUrl);
                startActivity(intent);

            }
            if (msg.what==3){
                attempLogin();
                //开启权限

            }
        }
    };
    EditText numberEditext;
    EditText passwordEditext;
    String data;
    String webUrl;

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isLogin=checkLogin();
        if (!isLogin){
            Toast.makeText(getApplicationContext(),"授权失败，请重新授权",Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startface);
        checkLogin();




    }

    public boolean checkLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences("user", MODE_PRIVATE);
        String status=sharedPreferences.getString("status", "");
        String userid=sharedPreferences.getString("userid", "");
        //Log.d("status",status);
        if (status.equals("success")){
            dataRequest(userid);
            return true;
        }else {
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
                    msg.what=3;
                    handler.sendMessage(msg);
                }
            };
            Data.isRequestNotfication=false;
            thread.start();
            return false;
        }

    }

    public void saveSelfInfo(SharedPreferences.Editor editor){
        //易班接口获取个人信息

        try {
            JSONObject jsonObject=new JSONObject(data);
            String info=jsonObject.getString("userInfo");
            Log.d("userInfo",info);
            String[] infos=info.split("&&");
            String school="华南农业大学";
            String sex=infos[2];
            String stuID=infos[3];
            String ID=infos[0];
            editor.putString("school",school);
            editor.putString("sex",sex);
            editor.putString("ID", ID);
            editor.putString("stuID",stuID);
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void attempLogin(){
        setContentView(R.layout.activity_login);
        Button loginButton=(Button)findViewById(R.id.login_button);
        //numberEditext=(EditText)findViewById(R.id.number);
        //passwordEditext=(EditText)findViewById(R.id.pass_word);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthorizeRequest(null, null);

            }
        });

        Button visitButton=(Button)findViewById(R.id.visit_button);
        //numberEditext=(EditText)findViewById(R.id.number);
        //passwordEditext=(EditText)findViewById(R.id.pass_word);
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("userid", "123");
                Log.d("status","success");
                editor.putString("status", "success");
                editor.commit();
                checkLogin();

            }
        });


    }

    public void dataRequest(final String stuID){
        //请求地址
        String url = "http://116.62.169.215:8080/MyWeb/DataServlet";    //注①
        String tag = "Data";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);  //注③
                            int size=jsonObject.getInt("size");
                            data=response;
                            Message msg=new Message();
                            msg.what=1;
                            handler.sendMessage(msg);
                            //Log.d("msg",response);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"无网络连接",Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"响应错误",Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
               // params.put("AccountNumber", accountNumber);  //注⑥
                //params.put("Password", password);
                params.put("stuID",stuID);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public void AuthorizeRequest(final String accountNumber, final String password) {
        //请求地址
        final String url = "http://116.62.169.215:8080/MyWeb/LoginServlet";    //注①
        String tag = "Login";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        webUrl=response;
                        Message msg=new Message();
                        msg.what=2;
                        handler.sendMessage(msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"响应错误",Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {

        };

        //设置Tag标签
        request.setTag(tag);
        Log.d("url",request.toString());
        //将请求添加到队列中
        requestQueue.add(request);
    }


}
