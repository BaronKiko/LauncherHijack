package com.gitxpresso.launcherhijack;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.UI_MODE_SERVICE;

public class SettingsMan {
    private static SettingStore settingStore;
    private static Context appContext;

    public static void init(Context context) {
        if (context == null) return;
        appContext = context.getApplicationContext();
        if (settingStore == null) {
            settingStore = new SettingStore(appContext);
        }
    }

    public static boolean isNativeServiceEnabled() {
        if (appContext == null) return false;
        return appContext.getSharedPreferences("LauncherHijack", MODE_PRIVATE)
                .getBoolean("NativeServiceEnabled", false);
    }

    public static void setNativeService(boolean enabled) {
        if (appContext == null) return;
        appContext.getSharedPreferences("LauncherHijack", MODE_PRIVATE)
                .edit()
                .putBoolean("NativeServiceEnabled", enabled)
                .apply();
        
        if (enabled) ServiceMan.Start(appContext);
        else ServiceMan.Stop(appContext);
    }

    public static SettingStore GetSettings() {
        return settingStore;
    }

    public static class SettingStore {
        public boolean HardwareDetection, ApplicationOpenDetection, BroadcastRecieverDetection, 
                       OverlayApplicationDetection, MenuButtonOverride, RecentAppOverride;
        private SharedPreferences settings;

        public SettingStore(Context c) {
            settings = c.getSharedPreferences("LauncherHijack", MODE_PRIVATE);
            if (!settings.getBoolean("defaultsLoaded", false)) {
                LoadDefaults();
                return;
            }
            HardwareDetection = settings.getBoolean("HardwareDetection", false);
            ApplicationOpenDetection = settings.getBoolean("ApplicationOpenDetection", false);
            BroadcastRecieverDetection = settings.getBoolean("BroadcastRecieverDetection", false);
            OverlayApplicationDetection = settings.getBoolean("OverlayApplicationDetection", false);
            MenuButtonOverride = settings.getBoolean("MenuButtonOverride", false);
            RecentAppOverride = settings.getBoolean("RecentAppOverride", false);
        }

        public void LoadDefaults() {
            if (appContext == null) return;
            UiModeManager uiModeManager = (UiModeManager) appContext.getSystemService(UI_MODE_SERVICE);
            boolean tv = (uiModeManager != null && uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
            
            HardwareDetection = MenuButtonOverride = tv;
            BroadcastRecieverDetection = true;
            SaveSettings();
        }

        public void SaveSettings() {
            settings.edit()
                .putBoolean("defaultsLoaded", true)
                .putBoolean("HardwareDetection", HardwareDetection)
                .putBoolean("ApplicationOpenDetection", ApplicationOpenDetection)
                .putBoolean("BroadcastRecieverDetection", BroadcastRecieverDetection)
                .putBoolean("OverlayApplicationDetection", OverlayApplicationDetection)
                .putBoolean("MenuButtonOverride", MenuButtonOverride)
                .putBoolean("RecentAppOverride", RecentAppOverride)
                .apply();
        }
    }
}