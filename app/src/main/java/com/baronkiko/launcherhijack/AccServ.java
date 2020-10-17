package com.baronkiko.launcherhijack;

import android.accessibilityservice.AccessibilityService;
import android.app.UiModeManager;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.jaredrummler.android.device.DeviceName;


public class AccServ extends AccessibilityService {

    static final String TAG = "AccServ";
    static boolean HomePressCanceled = false;
    static HomeWatcher homeWatcher;
    static String lastApp, lastClass;
    private static SettingsMan.SettingStore settings;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        lastApp = (String) event.getPackageName();
        lastClass = (String) event.getClassName();

        if (!settings.ApplicationOpenDetection)
            return;

        CharSequence packageName = event.getPackageName();
        if (packageName != null && packageName.equals("com.amazon.firelauncher"))
            HomePress.Perform(getApplicationContext());
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }

    @Override
    public boolean onKeyEvent(KeyEvent event)
    {
        if (!settings.HardwareDetection)
            return false;

        switch (event.getKeyCode())
        {
            case KeyEvent.KEYCODE_HOME:
                int action = event.getAction();

                if (action == KeyEvent.ACTION_UP)
                    HomePressCanceled = false;
                else if (action == KeyEvent.ACTION_DOWN && !HomePressCanceled)
                {
                    HomePress.Perform(getApplicationContext());
                    return true;
                }

                return false;

            case KeyEvent.KEYCODE_MENU:
                if (settings.MenuButtonOverride && event.getAction() == KeyEvent.ACTION_DOWN)
                    HomePressCanceled = true;
                return false;
        }
        return false;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        MainActivity.SetContext(getApplicationContext());

        settings = SettingsMan.GetSettings();

        lastClass = "";
        lastApp = "";

        homeWatcher = new HomeWatcher(getApplicationContext());
        homeWatcher.setOnHomePressedListener(new OnHomePressedListener()
        {
            @Override
            public void onHomePressed()
            {
                if (settings.BroadcastRecieverDetection && !HomePressCanceled &&
                   (!settings.RecentAppOverride | !(lastApp.equals("com.android.systemui") && lastClass.equals("com.android.systemui.recents.RecentsActivity"))))
                {
                    Log.d("New Home", "Home Press but new. LastApp: " + lastApp + "  LastClass: " + lastClass);
                    HomePress.Perform(getApplicationContext());
                }
            }

            @Override
            public void onRecentAppPressed()
            {
            }
        });
        homeWatcher.startWatch();

        Log.v(TAG, "Launcher Hijack Service Started on " + DeviceName.getDeviceName());
        HomePress.Perform(getApplicationContext());
    }

}