package com.wangsijiu.app;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2019/9/5.
 */
public class Utils {
    public static boolean NotifyEnable(Context context){
        NotificationManagerCompat notificationManagerCompat=(NotificationManagerCompat.from(context));
        return notificationManagerCompat.areNotificationsEnabled();
    }

    public static void checkNotifySetting(final Context context){
        if(!NotifyEnable(context)){
            Log.d("check", "check");

            MaterialDialog.Builder builder=new MaterialDialog.Builder(context)
                    .content("请在通知管理中→作业通知渠道→开启悬浮通知和锁屏通知")
                    .title("请开启通知权限")
                    .iconRes(R.drawable.app_icon)
                    .maxIconSize(100)
                    .positiveText("确认")
                    .negativeText("取消");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        Intent intent = new Intent();
                       /* intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                       */
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        context.startActivity(intent);
                    }
                }
            });
            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public static void requestNotification(final Context context){
        MaterialDialog.Builder builder=new MaterialDialog.Builder(context)
                .content("请在通知管理中→作业通知渠道→开启悬浮通知和锁屏通知")
                .title("需要通知权限")
                .iconRes(R.drawable.app_icon)
                .maxIconSize(100)
                .positiveText("确认")
                .negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                if (Build.VERSION.SDK_INT >= 26) {
                    Intent intent = new Intent();
                       /* intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                       */
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.show();
        Data.isRequestNotfication=true;
    }


    //检查service是否存活
    public static void checkService(Context context){
        if(!Data.serviceIsRunning) {
            //启动服务
            Intent intent = new Intent(context, KeepLiveService.class);
            context.startService(intent);
            Log.d("service","service is not live");
        }else {
            Log.d("service","service is  live");
        }
    }



}
