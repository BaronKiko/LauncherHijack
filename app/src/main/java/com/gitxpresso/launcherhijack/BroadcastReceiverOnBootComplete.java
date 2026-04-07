package com.gitxpresso.launcherhijack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

public class BroadcastReceiverOnBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsMan.init(context);
        
        // Start Service immediately
        if (SettingsMan.isNativeServiceEnabled()) {
            Intent serviceIntent = new Intent(context, HomeButtonService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }

        // AGGRESSIVE BOOT HIJACK:
        // We run it once immediately, and again after 2 seconds to catch 
        // the Fire Launcher if it tries to pop up late.
        HomePress.Perform(context);

        new Handler().postDelayed(() -> {
            HomePress.Perform(context);
        }, 2500);
    }
}