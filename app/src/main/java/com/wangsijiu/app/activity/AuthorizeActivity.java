package com.wangsijiu.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Message;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangsijiu.app.R;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AuthorizeActivity extends Activity {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                finish();
            }
        }
    };
    ZLoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        dialog=new ZLoadingDialog(AuthorizeActivity.this);
        String url=getIntent().getStringExtra("url");
        final WebView wb=(WebView)findViewById(R.id.wb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.setLoadingBuilder(Z_TYPE.PAC_MAN)
                        .setLoadingColor(R.color.APPBcolor3)//颜色
                        .setHintText("Loading...")
                        .setCanceledOnTouchOutside(false)
                        .show();

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.cancel();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                Log.d("urlL",url);
                if (!url.contains("yiban.cn")) {
                    wb.setVisibility(View.GONE);
                    BackRequest(url);
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });//添加WebViewClient实例
        wb.loadUrl(url);

    }

    public void BackRequest(final String webUrl) {
        //请求地址
        final String url=webUrl.split("\\?")[0] ;    //注①
        //Log.d("url",url);
        String tag = "Back";    //注②

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
                     Log.d("BackInfo", response);
                        try {
                            String[] rsp=response.split("&&");
                            SharedPreferences sharedPreferences=getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("userid", rsp[1]);
                            JSONObject jsonObject = new JSONObject(rsp[2]);
                            Log.d("status",jsonObject.getString("status"));
                            Log.d("info", jsonObject.getString("info"));
                            editor.putString("status", jsonObject.getString("status"));
                            editor.putString("info", jsonObject.getString("info"));
                            editor.commit();
                            Message msg=new Message();
                            msg.what=1;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"授权错误，请重新授权",Toast.LENGTH_SHORT).show();
                finish();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();//注⑥
                params.put("code",webUrl.split("=|&")[1]);
                Log.d("url",url);
                Log.d("code",webUrl.split("=|&")[1]);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        Log.d("url", request.toString());
        //将请求添加到队列中
        requestQueue.add(request);
    }

    public void InfoRequest(final String token) {
        //请求地址
        final String url="https://openapi.yiban.cn/user/me" ;    //注①

        String tag = "Info";    //注②

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
                    Log.d("url",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"响应错误",Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();//注⑥
                params.put("access_token", token);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        Log.d("url", request.toString());
        //将请求添加到队列中
        requestQueue.add(request);
    }

}
