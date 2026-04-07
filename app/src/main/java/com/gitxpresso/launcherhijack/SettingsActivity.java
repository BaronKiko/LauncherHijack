package com.gitxpresso.launcherhijack;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox hwButtonDetection, launcherOpen, broadcastReceiver, overlayDetection, 
                     disableWhileMenuHeld, disableInTaskSwitcher, nativeServiceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);
        
        SettingsMan.init(this);
        SettingsMan.SettingStore settings = SettingsMan.GetSettings();

        // Bind Checkboxes to the UI
        hwButtonDetection = findViewById(R.id.hardwareCB);
        launcherOpen = findViewById(R.id.openApplicationCB);
        broadcastReceiver = findViewById(R.id.broadcastCB);
        overlayDetection = findViewById(R.id.overlayCB);
        disableWhileMenuHeld = findViewById(R.id.menuButtonOverrideCB);
        disableInTaskSwitcher = findViewById(R.id.taskSwitchCB);
        nativeServiceToggle = findViewById(R.id.nativeServiceCB);

        // Load existing state
        hwButtonDetection.setChecked(settings.HardwareDetection);
        launcherOpen.setChecked(settings.ApplicationOpenDetection);
        broadcastReceiver.setChecked(settings.BroadcastRecieverDetection);
        overlayDetection.setChecked(settings.OverlayApplicationDetection);
        disableWhileMenuHeld.setChecked(settings.MenuButtonOverride);
        disableInTaskSwitcher.setChecked(settings.RecentAppOverride);
        nativeServiceToggle.setChecked(SettingsMan.isNativeServiceEnabled());

        // Attach click listeners to rows for better TV remote navigation
        AddRowListeners(findViewById(R.id.hardwareCBView), hwButtonDetection);
        AddRowListeners(findViewById(R.id.openApplicationCBView), launcherOpen);
        AddRowListeners(findViewById(R.id.broadcastCBView), broadcastReceiver);
        AddRowListeners(findViewById(R.id.overlayCBView), overlayDetection);
        AddRowListeners(findViewById(R.id.menuButtonOverrideCBView), disableWhileMenuHeld);
        AddRowListeners(findViewById(R.id.taskSwitchCBView), disableInTaskSwitcher);
        AddRowListeners(findViewById(R.id.nativeServiceCBView), nativeServiceToggle);

        findViewById(R.id.saveButton).setOnClickListener(v -> {
            settings.HardwareDetection = hwButtonDetection.isChecked();
            settings.ApplicationOpenDetection = launcherOpen.isChecked();
            settings.BroadcastRecieverDetection = broadcastReceiver.isChecked();
            settings.OverlayApplicationDetection = overlayDetection.isChecked();
            settings.MenuButtonOverride = disableWhileMenuHeld.isChecked();
            settings.RecentAppOverride = disableInTaskSwitcher.isChecked();
            
            SettingsMan.setNativeService(nativeServiceToggle.isChecked());
            settings.SaveSettings();
            
            Toast.makeText(this, "Settings Applied", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }

    private void AddRowListeners(final View view, final CheckBox checkBox) {
        view.setOnClickListener(v -> checkBox.toggle());
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                v.setBackgroundColor(Color.parseColor("#33AAAAAA")); // Subtle highlight
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                v.setBackgroundColor(Color.TRANSPARENT);
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settingsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.loadDefaults) {
            SettingsMan.GetSettings().LoadDefaults();
            recreate(); // Restart activity to show defaults
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}