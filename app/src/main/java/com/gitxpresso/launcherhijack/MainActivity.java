package com.gitxpresso.launcherhijack;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListAppInfo;
    private MenuItem launcher, sysApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SettingsMan.init(this);
        mListAppInfo = findViewById(R.id.lvApps);

        checkPermissions();
        UpdateList();

        mListAppInfo.setOnItemClickListener((parent, view, pos, id) -> {
            final ResolveInfo appInfo = (ResolveInfo) parent.getAdapter().getItem(pos);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Set Launcher")
                    .setMessage("Use " + appInfo.loadLabel(getPackageManager()) + "?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        SharedPreferences settings = getSharedPreferences("LauncherHijack", MODE_PRIVATE);
                        settings.edit()
                            .putString("ChosenLauncher", appInfo.activityInfo.packageName)
                            .putString("ChosenLauncherName", appInfo.activityInfo.name)
                            .apply();
                        Toast.makeText(MainActivity.this, "Launcher Set", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void checkPermissions() {
        // 1. Accessibility Check
        if (!isAccessibilityEnabled()) {
            new AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("Please enable Launcher Hijack in Accessibility settings.")
                .setPositiveButton("Settings", (d, w) -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)))
                .setCancelable(false).show();
        }

        // 2. Device Admin Check
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminName = new ComponentName(this, AdminReceiver.class);
        if (dpm != null && !dpm.isAdminActive(adminName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required to keep the service running smoothly.");
            startActivity(intent);
        }

        // 3. BATTERY POPUP (Restored per request)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // Fail silently if OS blocks the popup
                }
            }
        }
    }

    private void UpdateList() {
        if (mListAppInfo == null) return;
        boolean sys = (sysApps == null) || sysApps.isChecked();
        boolean l = (launcher == null) || launcher.isChecked();
        List<ResolveInfo> appInfo = Utilities.getInstalledApplication(this, l, sys);
        mListAppInfo.setAdapter(new AppAdapter(this, appInfo, getPackageManager()));
    }

    private boolean isAccessibilityEnabled() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (am == null) return false;
        String id = getPackageName() + "/.AccServ";
        for (AccessibilityServiceInfo service : am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            if (id.equals(service.getId())) return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        sysApps = menu.findItem(R.id.sysApps);
        launcher = menu.findItem(R.id.launcher);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        item.setChecked(!item.isChecked());
        UpdateList();
        return true;
    }
}