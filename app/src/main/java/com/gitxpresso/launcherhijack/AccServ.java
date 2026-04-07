package com.gitxpresso.launcherhijack;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class AccServ extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            CharSequence pkgName = event.getPackageName();
            if (pkgName == null) return;

            String pkg = pkgName.toString();

            // FIX: Ignore the Task Switcher / System UI
            if (pkg.equals("com.android.systemui")) {
                return; 
            }

            // Hijack if Fire Launcher is detected
            if (pkg.equals("com.amazon.firelauncher") || 
                pkg.equals("com.amazon.tv.launcher") ||
                pkg.equals("com.amazon.firehomestarter")) {
                
                HomePress.Perform(this);
            }
        }
    }

    @Override public void onInterrupt() {}

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        SettingsMan.init(this);
    }
}