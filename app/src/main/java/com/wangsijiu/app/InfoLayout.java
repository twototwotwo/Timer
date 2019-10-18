package com.wangsijiu.app;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 2019/8/1.
 */
public class InfoLayout extends LinearLayout{
    TextView infoTitleView;
    EditText infoView;
    ImageView headView;
    public InfoLayout(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);



    }

    public InfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);

    }
    public  void init(Point size,String infoTitle){
        //this.setPadding(0,0,0,0);
        this.setBackgroundResource(R.drawable.info_layout_shape);
        infoTitleView=new TextView(getContext());
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(10,0,10,0);
        params.weight=0;
        infoTitleView.setLayoutParams(params);
        infoTitleView.setText(infoTitle);
        infoTitleView.setGravity(Gravity.CENTER);
        //infoTitleView.setBackgroundColor(Color.WHITE);
        this.addView(infoTitleView);

        //LinearLayout line=new LinearLayout(getContext());
        //line.setBackgroundColor(Color.BLACK);
        //line.setLayoutParams(new LayoutParams(2, ));
        //this.addView(line);

        infoView=new EditText(getContext());
        LayoutParams params1=new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.setMarginEnd(5);
        params1.weight=1;
        infoView.setLayoutParams(params1);
        infoView.setAlpha(0.5f);
        infoView.setBackground(null);
        infoView.setPadding(10, 0, 0, 0);
        infoView.setGravity(Gravity.CENTER_VERTICAL);

        this.addView(infoView);
    }

    public void init(String infoTitle,String info,Point size){
        this.setBackgroundResource(R.drawable.info_layout_shape);
        infoTitleView=new TextView(getContext());
        LayoutParams params=new LayoutParams(0,size.y/20);
        params.setMarginStart(5);
        params.weight=1;
        infoTitleView.setLayoutParams(params);
        infoTitleView.setText(infoTitle);
        infoTitleView.setGravity(Gravity.CENTER_VERTICAL);
           this.addView(infoTitleView);

        infoView=new EditText(getContext());
        LayoutParams params1=new LayoutParams(0,size.y/20);
        params1.setMarginEnd(5);
        params1.weight=1;
        infoView.setLayoutParams(params1);
        infoView.setAlpha(0.5f);
        infoView.setBackground(null);
        infoView.setText(info);
        infoView.setPadding(10,0,0,0);
        infoView.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);

        this.addView(infoView);
    }
    //头像，重载
    public void init(String infoTitle,Point size){
        infoTitleView=new TextView(getContext());
        LayoutParams params=new LayoutParams(0,size.y/10);
        params.weight=1;
        infoTitleView.setLayoutParams(params);
        infoTitleView.setText(infoTitle);
        infoTitleView.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(infoTitleView);

        headView=new ImageView(getContext());
        LayoutParams params1=new LayoutParams(size.y/10, size.y/10);
        params1.weight=0;
        headView.setLayoutParams(params1);

        //头像
        if (Data.head!=null){
            headView.setImageBitmap(Data.head);
        }else {
            headView.setImageResource(R.drawable.head);
        }
        headView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        this.addView(headView);
    }

    public ImageView getHeadView() {
        return headView;
    }

    public void setHeadView(ImageView headView) {
        this.headView = headView;
    }

    public String getInfo(){
        return infoView.getText().toString();


    }

    public void setUnFocuse(){
        this.infoView.setFocusable(false);
    }

}
