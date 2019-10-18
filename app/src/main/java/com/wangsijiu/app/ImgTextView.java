package com.wangsijiu.app;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/7/10.
 */
public class ImgTextView extends CardView {
    CardLayout cardLayout;
    public ImgTextView(Context context,int ImgRes, final String str,Point size)
    {
        super(context);
        cardLayout=new CardLayout(context,ImgRes,str,size);
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        cardLayout.setLayoutParams(params);
        this.addView(cardLayout);
        this.setRadius(30f);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    class CardLayout extends LinearLayout{
        ImageView icon;
        TextView title;
        ImageView arrow;
        int height;
        int width;
        public CardLayout(Context context,int ImgRes,String str,Point size){
            super(context);

            this.height=size.y/15;
            this.width=size.x;
            icon=new ImageView(context);
            title=new TextView(context);
            arrow=new ImageView(context);
            setIcon(ImgRes);
            setTitle(str);
            arrow.setImageResource(R.drawable.arrow);
            this.setOrientation(HORIZONTAL);
            this.init();
        }

        public void init(){

            LayoutParams iconParams=new LayoutParams(height/2, height/2);
            iconParams.gravity=Gravity.LEFT|Gravity.TOP;
            iconParams.setMargins(height/4,height/4,height/4,height/4);
            icon.setLayoutParams(iconParams);
            this.addView(icon);

            //设置按钮文本

            LayoutParams titleParams=new LayoutParams(width-2*height, height);
            titleParams.gravity=Gravity.CENTER;
            title.setTextSize(16);
            title.setGravity(Gravity.CENTER_VERTICAL);
            title.setLayoutParams(titleParams);
            this.addView(title);

            LayoutParams arrowParams=new LayoutParams(height/2,height/2);
            arrowParams.gravity=Gravity.RIGHT|Gravity.TOP;
            arrowParams.setMargins(height/4,height/4,height/4,height/4);
            arrow.setLayoutParams(arrowParams);
            this.addView(arrow);
        }

        public void setIcon(int resId){
            icon.setImageResource(resId);
        }
        public void setTitle(String str){
            title.setText(str);
        }

    }
}
