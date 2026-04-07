package com.gitxpresso.launcherhijack;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ServiceMan {
    public static void Start(Context context) {
        Intent serviceIntent = new Intent(context, HomeButtonService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public static void Stop(Context context) {
        Intent serviceIntent = new Intent(context, HomeButtonService.class);
        context.stopService(serviceIntent);
    }
}