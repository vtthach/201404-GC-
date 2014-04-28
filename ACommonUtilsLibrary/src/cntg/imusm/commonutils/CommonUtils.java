package cntg.imusm.commonutils;
/**
 
 */


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author bthanhphong
 * @CreatedDate Feb 13, 2014
 * @Description: TODO
 */
@SuppressLint("NewApi")
public class CommonUtils {

	/**
	 * @Thach Feb 21, 2014
	 * @Desc lock Orientation
	 * @param b
	 */
	@SuppressWarnings("deprecation")
	public static void lockOrientation(Activity act, boolean isLock) {
		if (act == null) {
			return;
		}
		if (isLock) {
			Display display = act.getWindowManager().getDefaultDisplay();
			int rotation = display.getRotation();
			int height;
			int width;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
				height = display.getHeight();
				width = display.getWidth();
			} else {
				Point size = new Point();
				display.getSize(size);
				height = size.y;
				width = size.x;
			}
			switch (rotation) {
			case Surface.ROTATION_90:
				if (width > height)
					act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				else
					act.setRequestedOrientation(9/* reversePortait */);
				break;
			case Surface.ROTATION_180:
				if (height > width)
					act.setRequestedOrientation(9/* reversePortait */);
				else
					act.setRequestedOrientation(8/* reverseLandscape */);
				break;
			case Surface.ROTATION_270:
				if (width > height)
					act.setRequestedOrientation(8/* reverseLandscape */);
				else
					act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			default:
				if (height > width)
					act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				else
					act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} else {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}

	public static void hideKeyboard(Context context, View edt) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
	}

	/**
	 * @Description:
	 * @param applicationContext
	 * @return
	 */
	public static int getHeightActionBar(Context context) {
		int height;
		TypedValue typeValue = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
				typeValue, true);
		height = TypedValue.complexToDimensionPixelSize(typeValue.data, context
				.getResources().getDisplayMetrics());
		return height;
	}

	public static String elapsedToString(long elapsedTimeMillis,
			boolean isPlaying) {
		if (isPlaying) {
			long hundredths = (elapsedTimeMillis + 5) / 10; // round
			long seconds = hundredths / 100;
			long minutes = seconds / 60;
			return String.format("%1$02d:%2$02d", minutes, seconds % 60);
		} else {

			long hundredths = (elapsedTimeMillis + 5) / 10; // round
			long seconds = hundredths / 100;
			// long minutes = seconds / 60;
			return String.format("%1$02d.%2$02d", seconds, hundredths % 100);
		}
	}


	/**
	 * @Description:
	 * @param addNoteActivity
	 */
	public static void forceShowKeyboard(Context context, EditText edt) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInputFromInputMethod(edt.getWindowToken(),
				InputMethodManager.SHOW_FORCED);
	}


	
	/** 
	 * @Thach
	 * @Description: on save data to SharedPref with given key 
	 * @param context
	 * @param key 
	 * @param msg data will be save
	 */
	public static void onSavePref(Context context, String key, String msg) {
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
		pre.edit().putString(key, msg).commit();
	}

	/** 
	 * @Thach
	 * @Description: on get data to SharedPref with given key 
	 * @param context
	 * @param key 
	 * @param msg data will be save
	 */
	public static String onGetPref(Context context, String key) {
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
		return pre.getString(key, null);
	}
	
	/**
	 * @Thach
	 * @Description: get and save deviceId into sharePreference
	 * @param context
	 * @return deviceId 
	 */
	public static String getDeviceId(Context context, String keyPref){
		String deviceId = null;
		if (context != null) {
			// Get from sharePref first 
			deviceId = onGetPref(context, keyPref);
			// If this is first time
			if(deviceId == null){
				TelephonyManager telephonyManager;
				telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = telephonyManager.getDeviceId();
				// Get deviceId with other method - MAC address 
				if(deviceId == null){
					deviceId = initMacAddress(context);
				}
			}
			// save to preference
			onSavePref(context, keyPref, deviceId);
		}
		return deviceId;
	}
	/**
	 * @Thach @SK29
	 * @Init MAC address and save to preference
	 * @param context
	 * @return
	 */
	private static String initMacAddress(Context context) {
		// INIT macAddress
		String macAddress;

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		if (wifiManager.isWifiEnabled()) {
			// WIFI ALREADY ENABLED. GRAB THE MAC ADDRESS HERE
			WifiInfo info = wifiManager.getConnectionInfo();
			macAddress = info.getMacAddress();

		} else {
			// ENABLE THE WIFI FIRST
			wifiManager.setWifiEnabled(true);

			// WIFI IS NOW ENABLED. GRAB THE MAC ADDRESS HERE
			WifiInfo info = wifiManager.getConnectionInfo();
			macAddress = info.getMacAddress();

			// NOW DISABLE IT AGAIN
			wifiManager.setWifiEnabled(false);
		}

		if (macAddress == null) {
			macAddress = android.os.Build.ID;
		}

		return macAddress;
	}

	
	public static boolean isListValid(List<?> list) {
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isIndexInListValid(List<?> list, int index) {
		if (list != null && list.size() > 0) {
			if (index >= 0 && index < list.size()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
