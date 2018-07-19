package com.baronkiko.launcherhijack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class AccServ extends AccessibilityService {

    static final String TAG = "AccServ";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getPackageName().equals("com.amazon.firelauncher") || event.getPackageName().equals("com.amazon.tv.launcher"))
            HomePress.Perform(getApplicationContext());
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        ServiceMan.Start(getApplicationContext());

        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
/* FireOS 5
 *      info.packageNames = new String[]{"com.amazon.firelauncher"};
 * FireOS 8 */
        info.packageNames = new String[]{"com.amazon.tv.launcher"};
        setServiceInfo(info);
        HomePress.Perform(getApplicationContext());
    }

}
