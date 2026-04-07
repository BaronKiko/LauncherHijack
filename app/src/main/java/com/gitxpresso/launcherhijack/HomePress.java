package com.gitxpresso.launcherhijack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class HomePress {
    public static void Perform(Context context) {
        SharedPreferences settings = context.getSharedPreferences("LauncherHijack", Context.MODE_PRIVATE);
        String pkg = settings.getString("ChosenLauncher", "");
        String cls = settings.getString("ChosenLauncherName", "");

        if (!pkg.isEmpty() && !cls.isEmpty()) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(pkg, cls));
            
            // FLAG_ACTIVITY_NO_ANIMATION fixes the "Home Button Spam" bug
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                          | Intent.FLAG_ACTIVITY_CLEAR_TOP 
                          | Intent.FLAG_ACTIVITY_SINGLE_TOP 
                          | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            
            context.startActivity(intent);
        }
    }
}