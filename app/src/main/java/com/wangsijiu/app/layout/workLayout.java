package com.wangsijiu.app.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsijiu.app.AddWorkLayout;
import com.wangsijiu.app.Data;
import com.wangsijiu.app.KeepLiveService;
import com.wangsijiu.app.R;
import com.wangsijiu.app.work;
import com.wangsijiu.app.workView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/7/4.
 */
public class workLayout extends RelativeLayout {
    LinearLayout findLayout;
    RecyclerView work;
    ImageButton addButton;

    EditText findText;
    AddWorkLayout addWorkLayout;
    ArrayList<work> datas;

    Point size;
    String data;
    public  workLayout(Context context,Point size,String data){
        super(context);
        Log.d("data", data);
        this.data=data;
        this.size=size;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0);
        params.weight=12;
        params.gravity= Gravity.CENTER_VERTICAL;
        //this.setOrientation(VERTICAL);
        this.setLayoutParams(params);
        this.setBackgroundColor(getResources().getColor(R.color.APPBcolor1));
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.init();
    }

    public void init(){
       findLayout=new LinearLayout(getContext());
         RelativeLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,size.y/15);
        findLayout.setLayoutParams(params);
        findLayout.setOrientation(LinearLayout.HORIZONTAL);
        findLayout.setBackgroundColor(Color.WHITE);
        findLayout.setAlpha(0.5f);
        this.addView(findLayout);

        ImageView findButton=new ImageView(getContext());
        findButton.setLayoutParams(new ViewGroup.LayoutParams(size.y / 15
                , size.y / 15));
        findButton.setImageResource(R.drawable.findicon);
        findLayout.addView(findButton);
        findButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String target = findText.getText().toString();
                Log.d("target", target);
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).subject.contains(target)) {
                        LinearLayoutManager manager = (LinearLayoutManager) work.getLayoutManager();
                        manager.scrollToPosition(i);
                        manager.setStackFromEnd(true);
                        break;
                    } else if (i == datas.size() - 1) {
                        Toast.makeText(getContext(), "没有该作业", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        findText=new EditText(getContext());
        findText.setLayoutParams(new ViewGroup.LayoutParams(size.x - 2 * size.y / 15, size.y / 15));
        findText.setBackgroundColor(Color.WHITE);
        findText.setAlpha(0.5F);
        findLayout.addView(findText);
        findText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findText.setFocusable(true);
                findText.setFocusableInTouchMode(true);
                //Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();

            }
        });
        final TextView cancel=new TextView(getContext());
        cancel.setLayoutParams(new ViewGroup.LayoutParams(size.y / 15, size.y / 15));
        cancel.setText("取消");
        cancel.setTextSize(18f);
        cancel.setTextColor(Color.GRAY);
        findLayout.addView(cancel);
        final InputMethodManager imm= (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findText.clearFocus();
                imm.hideSoftInputFromWindow(findText.getWindowToken(), 0);
            }
        });



       work=new RecyclerView(getContext());
         LayoutParams params1=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
         params1.addRule(ALIGN_PARENT_BOTTOM);
         params1.setMargins(0, size.y / 15 + 10, 0, 0);
         work.setLayoutParams(params1);
         work.setLayoutManager(new LinearLayoutManager(getContext()));

         datas=changeToWork();
         WorkAdapter workAdapter=new WorkAdapter(datas);
         work.setAdapter(workAdapter);
         this.addView(work);

       addButton=new ImageButton(getContext());
        addButton.setBackgroundResource(R.drawable.add);
        LayoutParams params2=new LayoutParams(size.x/7,size.x/7);
        params2.addRule(ALIGN_PARENT_BOTTOM);
        params2.addRule(ALIGN_PARENT_RIGHT);
        params2.setMargins(0, 0, 10, size.y / 10);
        addButton.setLayoutParams(params2);
        addButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkLayout.setVisibility(VISIBLE);
                addWorkLayout.showTpView();
            }
        });
       this.addView(addButton);

       addWorkLayout=new AddWorkLayout(getContext(),size,this);
        addView(addWorkLayout);
        addWorkLayout.setVisibility(GONE);


        //作业数量
        Data.workSize=datas.size();
    }


    public void addWork(work data){
        datas.add(data);
        Data.workSize=datas.size();
        work.getAdapter().notifyItemChanged(datas.size()-1);


    }

    public ArrayList<work> changeToWork(){
        ArrayList<work> data=new ArrayList<work>();
        try{
            JSONObject jsonObject=new JSONObject(this.data);
            Log.d("userInfo",jsonObject.getString("userInfo"));
            for(int i=0;i<jsonObject.getInt("size");i++){
                 data.add(new work(jsonObject.getString(String.valueOf(i))));
            }

        }catch (JSONException je){

        }
        return data;
    }


    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder>{
        private ArrayList<work> data;
        class ViewHolder extends  RecyclerView.ViewHolder{
            workView work;
            public ViewHolder(View view){
                super(view);
                work=(workView)view;
            }
        }
        public  WorkAdapter(ArrayList<work> data){
          this.data=data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            WorkAdapter.ViewHolder holder=new WorkAdapter.ViewHolder(new workView(parent.getContext(),size));
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            work w=data.get(position);
            holder.work.init(w,position);
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }




}
