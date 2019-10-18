package com.wangsijiu.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wangsijiu.app.Data;
import com.wangsijiu.app.InfoLayout;
import com.wangsijiu.app.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfInfoActivity extends Activity {
    InfoLayout head;
    InfoLayout ID;
    InfoLayout sex;
    InfoLayout number;
    InfoLayout school;
    ImageView backButton;
    TextView changeButton;
    Bitmap image=null;
    Point size;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparent(0);//设置状态栏透明
        setContentView(R.layout.activity_self_info);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        size = new Point();
        wm.getDefaultDisplay().getSize(size);

        SharedPreferences sP = getSharedPreferences("user", MODE_PRIVATE);
        head = (InfoLayout) findViewById(R.id.head);
        head.init("头像", size);
        head.getHeadView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });
        ID = (InfoLayout) findViewById(R.id.name);
        ID.init("ID", sP.getString("ID", ""), size);
        sex = (InfoLayout) findViewById(R.id.sex);
        sex.init("性别", sP.getString("sex", ""), size);
        number = (InfoLayout) findViewById(R.id.number);
        number.init("学号", sP.getString("stuID", ""), size);
        //number.setUnFocuse();
        school = (InfoLayout) findViewById(R.id.school);
        school.init("学校", sP.getString("school", ""), size);

        changeButton = (TextView) findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存个人信息
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("school", school.getInfo());
                editor.putString("sex", sex.getInfo());
                editor.putString("ID", ID.getInfo());
                editor.putString("stuID", number.getInfo());
                editor.commit();
                String info = ID.getInfo() + "&&" + sharedPreferences.getString("userid", "") + "&&" + sex.getInfo()
                        + "&&" + number.getInfo();
                UpdateUserRequest(info);
                Data.head=image;

            }
        });

        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (uri != null) {
            Log.d("change","change");
            //在这里获得了剪裁后的Bitmap对象，可以用于上传

            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            };

            //设置到ImageView上
            head.getHeadView().setImageBitmap(image);

            head.invalidate();
            //也可以进行一些保存、压缩等操作后上传
//                    String path = saveImage("crop", image);
        }

    }

    private static final int REQUEST_PERMISSIONS = 1000;
    private void requestPermissons(){
        String[]  permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,//文件读写
                Manifest.permission.CAMERA,//相机
                Manifest.permission.RECORD_AUDIO//音频
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "用户曾拒绝xxxx", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }



    public void UpdateUserRequest(final String userInfo) {
        //请求地址
        String url = "http://116.62.169.215:8080/MyWeb/UpdateUserServlet";    //注①
        String tag = "Update";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        finish();
                        //Log.d("msg",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(), "响应错误", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("AccountNumber", accountNumber);  //注⑥
                //params.put("Password", password);
                params.put("userInfo", userInfo);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //设置状态栏透明，n为透明度
    public void setTransparent(int n) {
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
