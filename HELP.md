# Fire Tablets
**Installation**  
1. Navigate to this GitHub's downloads page (https://github.com/BaronKiko/LauncherHijack/releases) on your tablet and download the latest APK
2. Open your newly downloaded file and install like any other Android application
3. Open settings and navigate to "Accessibility -> To detect home button press" and press enable
4. If Launcher Hijack didn't open, find and open it
5. Select your desired Launcher from the list provided
6. Reboot your device
7. Congratulation, if all is working correctly your home button should now open your chosen launcher

**Updates (Specifically 5.6.2.0)**  
Please disable LauncherHijack in accessibility settings during Fire OS updates. LauncherHijack causes issues during update 5.6.2.0, you won't brick your device or anything with it left enabled but you won't be able to complete update 5.6.2.0 until disabled. There is nothing I can do without degrading the application during regular use. If you somehow end up mid update with LauncherHijack enabled the easiest solution, assuming you can't get to settings, is to uninstall LauncherHijack through ADB (plenty of guides online) and reinstall it after you complete the update.

**Google Now Launcher**  
The Google Now launcher is the only known launcher that supports widgets out of the box. It can however be a pain to set up so try following these instructions:
1. Ensure Launcher Hijack works with another launcher
2. Install the Google Search App ([Available Here](https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox))
3. Some devices also require Google home so probably best to install that too ([Available Here](https://play.google.com/store/apps/details?id=com.google.android.apps.chromecast.app));
4. Obviously install the Google Now launcher ([Available Here](https://play.google.com/store/apps/details?id=com.google.android.launcher))
5. REBOOT,
6. Open launcher hijack and select "Google", if you have to uncheck "Launchers only" something has gone wrong, so give another reboot a go and ensure you have the prerequisites listed above installed.
7. Google now should open when pressing home, if it still doesn't work uninstall all prerequisites and the launcher itself, reboot and try follow the guide again. I had to reinstall once to get it working however I have had reports of people reinstalling 4-5 times before finally getting it working.


**Widgets**  
Widget support in prohibited by default for all launchers except the Google Now launcher on Fire OS devices. You can attempt to enable them for a given launcher using the following adb command:  
`appwidget grantbind --package <launcher_package_name> --user current`  
You can get the package name with Launcher Hijack, in the confirmation dialog when selecting a launcher. The part contained within the brackets is the package name.  
Unfortunately I have had reports of mixed success with this command but it's worth trying if you want widgets for launchers other than the Google Now launcher.


**Disable the Default Launcher**  
If you are annoyed by the default launcher appearing while your chosen launcher is loading then you can corrupt the default launcher.  
Fair **WARNING** if you do this without any 3rd party launchers installed you will have a very hard time fixing your device and once the Fire Launcher is broken it can never be fixed for that user account, you will have to make a new user account to resurrect it.
1. Download the corrupted launcher found in the attached files of this [XDA post](https://forum.xda-developers.com/amazon-fire/general/tut-easily-remove-amazons-firelauncher-t3467758). You will need an XDA account to download the file.
2. Install it using the following command `adb install -r -d <path to downloaded file>`
3. Reboot

**Troubleshooting**  
If you are having issues then there are some common issues you can check here:
- Ensure you are using an Amazon device running Fire OS, compatible with all versions as of release (6.3 = latest)
- Ensure home button detection is enabled: "Settings -> Accessibility -> To detect home button press"
- If Launcher Hijack keeps saying "Accessibility Service Disabled" when opened despite "Settings -> Accessibility -> To detect home button press" being set to on:
  1. Go to "Settings -> Apps & Games -> Manage All Applications -> Launcher Hijack"
  2. Press menu
  3. Select "Uninstall for all users" then OK
  4. Power off your device
  5. Wait 5 seconds
  6. Boot
  7. Start at instillation step 1
- Ensure you can open your launcher directly i.e. Using any launcher, other than your chosen launcher, and open you chosen launcher like any other app, this should open your chosen launcher. CAVEAT: This does NOT work for the google now launcher and any launcher that requires default; When opening a launcher like this you will be redirected to the fire os system settings so that you can set your default launcher which is not possible, hence why this app was created
- Ensure you have selected your chosen launcher in Launcher Hijack
- Reboot your device after following all previous steps
- After clicking home initially wait 10 seconds and press home again. This happens when your chosen launchers process gets killed in the background and may happen from time to time, especially after a reboot. There are plenty of existing apps that already keep a chosen application in memory so try one of those if this is a reoccurring issue for you.
- If all else fails try using one of the following launchers with known compatibility: Nova Launcher, Evie, Arrow Launcher


# Fire TV's
**Installation**  
1. Navigate to this GitHub's downloads page (https://github.com/BaronKiko/LauncherHijack/releases) on your desktop/laptop and download the latest APK
2. Install ADB on your desktop/laptop (Google will help you) and ensure a connection to your Fire TV.
3. Install Launcher Hijack with this command  
`adb install <path to downloaded APK>`
4. Ensure you have some Launchers installed
5. Enable the accessibility service using these commands:  
`adb shell settings put secure enabled_accessibility_services com.baronkiko.launcherhijack/com.baronkiko.launcherhijack.AccServ`  
`adb shell settings put secure accessibility_enabled 1`
6. If Launcher Hijack didn't open, find and open it
7. Select your desired Launcher from the list provided
8. Reboot your device
9. Congratulation, if all is working correctly your home button should now open your chosen launcher

**Important, Accessing Settings and the Long Press Menu**  
Unfortunately for FireTV devices a compromise had to be made. To access the long press menu and with it settings you have to hold menu and home together. You can also get to the default launcher by simply pressing them together.

**Troubleshooting**  
If you are having issues then there are some common issues you can check here:
- Ensure you are using an Amazon TV device running Fire OS, compatible with all versions as of release (6.3 = latest)
- Ensure you have enabled the accessibility service with the command: `adb shell settings put secure enabled_accessibility_services com.baronkiko.launcherhijack/com.baronkiko.launcherhijack.AccServ`
- Ensure you can open your launcher directly i.e. Using any launcher, other than your chosen launcher, open you chosen launcher like any other app, this should open your chosen launcher
- Ensure you have selected your chosen launcher in Launcher Hijack
- Reboot your device after following all previous steps
- After clicking home initially wait 10 seconds and press home again. This happens when your chosen launchers process gets killed in the background and may happen from time to time, especially after a reboot. There are plenty of existing apps that already keep a chosen application in memory so try one of those if this is a reoccurring issue for you.
- If all else fails try using one of the following launchers with known compatibility: AppStarter or HALauncher
