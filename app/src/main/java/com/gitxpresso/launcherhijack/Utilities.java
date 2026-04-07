package com.gitxpresso.launcherhijack;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities {
    public static List<ResolveInfo> getInstalledApplication(Context c, boolean launchers, boolean systemApps) {
        PackageManager pm = c.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (launchers) intent.addCategory(Intent.CATEGORY_HOME);
        
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm));

        if (systemApps) return list;

        List<ResolveInfo> filtered = new ArrayList<>();
        for (ResolveInfo info : list) {
            if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                filtered.add(info);
            }
        }
        return filtered;
    }
}