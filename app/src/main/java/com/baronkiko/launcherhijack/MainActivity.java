package com.baronkiko.launcherhijack;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListAppInfo;
    private MenuItem launcher, sysApps;
    private int prevSelectedIndex = 0;

    public final static int REQUEST_CODE = 5466;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        sysApps = menu.getItem(0);
        launcher = menu.getItem(1);
        launcher.setChecked(true);
        sysApps.setChecked(true);
        UpdateList();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.launcher:
                launcher.setChecked(!launcher.isChecked());
                if (launcher.isChecked())
                    sysApps.setChecked(true);
                UpdateList();
                return true;

            case R.id.sysApps:
                sysApps.setChecked(!sysApps.isChecked());
                UpdateList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void UpdateList()
    {
        boolean sys = sysApps.isChecked();
        boolean l = launcher.isChecked();
        List<ApplicationInfo> appInfo = Utilities.getInstalledApplication(this, l, sys); // Get available apps

        mListAppInfo = (ListView) findViewById(R.id.lvApps);

        // create new adapter
        AppInfoAdapter adapter = new AppInfoAdapter(this, appInfo, getPackageManager());

        // set adapter to list view
        mListAppInfo.setAdapter(adapter);

        SharedPreferences settings = getSharedPreferences("LauncherHijack", MODE_PRIVATE);
        String selectedPackage = settings.getString("ChosenLauncher", "com.teslacoilsw.launcher");

        for (int i = 0; i < appInfo.size(); i++) {
            if (appInfo.get(i).packageName.equals(selectedPackage)) {
                prevSelectedIndex = i;
                mListAppInfo.setSelection(i);
                mListAppInfo.setItemChecked(i, true);
                break;
            }
        }
    }

    private void showSecurityAlert() {
        // Notify User
        String welcomeMessage = "The FireTV devices running Nougat or higher do not provide a UI to the Application Permissions.";
        String welcomeMessage2 = "In order to use this tool on your device, you must first run the below commands from your PC:";
        String adbCommand1 = "# adb tcpip 5555";
        String adbCommand2 = "# adb connect (yourfiretvip)";
        String adbCommand3 = "# adb shell";
        String adbCommand4 = "# pm grant com.baronkiko.launcherhijack android.permission.SYSTEM_ALERT";
        String adbCommand4Part2 = "    _WINDOW";
        String adbCommand5 = "# settings put secure enabled_accessibility_services com.baronkiko.laun";
        String adbCommand5Part2 = "    cherhijack/com.baronkiko.launcherhijack.AccServ";

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("FireTV Permissions Notice");

        LinearLayout alertContents = new LinearLayout(this);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView alertMessage = new TextView(this);
        TextView alertCommands1 = new TextView(this);
        TextView alertCommands1ExtraLine = new TextView(this);
        TextView alertCommands2 = new TextView(this);
        TextView alertCommands2ExtraLine = new TextView(this);

        alertMessage.setText(welcomeMessage + " " + welcomeMessage2 + "\n");
        alertMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        alertMessage.setTextColor(Color.WHITE);

        alertCommands1.setText(adbCommand1 + "\n" + adbCommand2 + "\n" + adbCommand3 + "\n" + adbCommand4);
        alertCommands1.setGravity(Gravity.LEFT);
        alertCommands1.setTextColor(Color.WHITE);

        alertCommands1ExtraLine.setText(adbCommand4Part2);
        alertCommands1ExtraLine.setGravity(Gravity.LEFT);
        alertCommands1ExtraLine.setTextColor(Color.WHITE);

        alertCommands2.setText(adbCommand5);
        alertCommands2.setGravity(Gravity.LEFT);
        alertCommands2.setTextColor(Color.WHITE);

        alertCommands2ExtraLine.setText(adbCommand5Part2);
        alertCommands2ExtraLine.setGravity(Gravity.LEFT);
        alertCommands2ExtraLine.setTextColor(Color.WHITE);

        alertContents.setLayoutParams(lllp);
        alertContents.setOrientation(LinearLayout.VERTICAL);
        alertContents.removeAllViews();
        alertContents.addView(alertMessage);
        alertContents.addView(alertCommands1);
        alertContents.addView(alertCommands1ExtraLine);
        alertContents.addView(alertCommands2);
        alertContents.addView(alertCommands2ExtraLine);

        alertDialog.setView(alertContents);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                }
                catch(SecurityException | ActivityNotFoundException e) {
                    showSecurityAlert();
                }
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkDrawOverlayPermission()) {
            ServiceMan.Start(this);
        }

        setContentView(com.baronkiko.launcherhijack.R.layout.activity_main);

        mListAppInfo = (ListView) findViewById(R.id.lvApps);

        // implement event when an item on list view is selected
        mListAppInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                // get the list adapter
                AppInfoAdapter appInfoAdapter = (AppInfoAdapter) parent.getAdapter();
                // get selected item on the list
                final ApplicationInfo appInfo = (ApplicationInfo) appInfoAdapter.getItem(pos);

                // Notify User
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Set Launcher");
                alertDialog.setMessage("Set your launcher to " + appInfo.loadLabel(getPackageManager()) + " (" + appInfo.packageName + ")");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prevSelectedIndex = pos;

                                // We need an Editor object to make preference changes.
                                // All objects are from android.context.Context
                                SharedPreferences settings = getSharedPreferences("LauncherHijack", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("ChosenLauncher", appInfo.packageName);
                                editor.commit(); // Commit the edits!
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mListAppInfo.setSelection(prevSelectedIndex);
                                mListAppInfo.setItemChecked(prevSelectedIndex, true);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (Settings.canDrawOverlays(this)) {
                ServiceMan.Start(this);
            }
        }
    }
}
