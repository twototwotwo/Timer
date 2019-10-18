package com.wangsijiu.app.layout;

import android.content.Context;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wangsijiu.app.R;

/**
 * Created by Administrator on 2019/7/19.
 */
public class schoolLayout extends LinearLayout {


    public schoolLayout(Context context,Point size) {
        super(context);
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        params.weight=12;
        this.setOrientation(VERTICAL);
        this.setLayoutParams(params);
        this.setBackgroundColor(getResources().getColor(R.color.APPBcolor1));


    }
}
