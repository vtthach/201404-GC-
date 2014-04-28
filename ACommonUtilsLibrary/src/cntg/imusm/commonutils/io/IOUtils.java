/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */

package cntg.imusm.commonutils.io ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author  team
 * @CreatedDate Mar 22, 2013
 * @Description: some common functions that need to be reused frequently.
 */
public class IOUtils
{
	
	/**
	 * @Description: check to see whether email address is valid or not
	 * 
	 * @Return boolean
	 */
	public static boolean isValidEmail(String inputMail)
	{
		
		if (inputMail == null || inputMail == "")
			{
				return false ;
			}
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+") ;
		return pattern.matcher(inputMail).matches() ;
	}
	
	public static String convertStreamToString(InputStream is)
	{
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to produce
		 * the string.
		 */
		
		if (is != null)
			{
				Writer writer = new StringWriter() ;
				
				char[] buffer = new char[1024] ;
				try
					{
						Reader reader = new BufferedReader(new InputStreamReader(is,
								"UTF-8")) ;
						int n ;
						while ((n = reader.read(buffer)) != -1)
							{
								writer.write(buffer, 0, n) ;
							}
					} catch (IOException e)
					{
						return "" ;
					} finally
					{
						try
							{
								is.close() ;
							} catch (IOException e)
							{
								
							}
					}
				return writer.toString() ;
			} else
			{
				return "" ;
			}
	}
	
	/**
	 * @Description: get current locale code
	 * 
	 * @Return String
	 */
	public static String currentLocale(Context context)
	{
		String locale = context.getResources().getConfiguration().locale
				.getLanguage() ;
		// Log.v("Locale", locale);
		return locale ;
	}
	
	/**
	 * @Description: convert string contain space to valid param to sert url
	 *               request
	 * 
	 * @Return String
	 */
	public static String validateSpaceChar(String request)
	{
		String temp = "" ;
		String[] arrayRequest = request.split(" ") ;
		if (arrayRequest.length <= 1)
			temp = request ;
		else
			for (int i = 0; i < arrayRequest.length - 1;)
				{
					temp = temp + arrayRequest[i] + "%20" ;
					i++ ;
					
					if (i + 1 == arrayRequest.length)
						temp = temp + arrayRequest[i] ;
				}
		
		String temp2 = "" ;
		String[] arrayRequest1 = temp.split("&") ;
		if (arrayRequest1.length <= 1)
			temp2 = temp ;
		else
			for (int i = 0; i < arrayRequest1.length - 1;)
				{
					temp2 = temp2 + arrayRequest1[i] + "%26" ;
					i++ ;
					if (i + 1 == arrayRequest1.length)
						temp2 = temp2 + arrayRequest1[i] ;
				}
		
		return temp2 ;
	}
	
	public static String trimTrailingZeros(String number)
	{
		if (!number.contains("."))
			{
				return number ;
			}
		
		return number.replaceAll(".?0*$", "") ;
	}
	

	
	
	/**
	 * 
	 * @param foldername
	 *           : name directory need to create
	 * @return true if success false if fail
	 */
	public static boolean createFolder(String foldername)
	{
		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ foldername) ;
		boolean success = false ;
		if (!folder.exists())
			{
				success = folder.mkdir() ;
			}
		if (!success)
			{
				return true ;
			} else
			{
				return false ;
			}
	}
	
	/**
	 * Function to convert milliseconds time to Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long milliseconds)
	{
		String finalTimerString = "" ;
		String minutesString = "" ;
		String secondsString = "" ;
		
		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60)) ;
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60) ;
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000) ;
		// Add hours if there
		if (hours > 0)
			{
				finalTimerString = hours + ":" ;
			}
		
		// Prepending 0 to minutes if it is one digit
		if (minutes < 10)
			{
				minutesString = "0" + minutes ;
			} else
			{
				minutesString = "" + minutes ;
			}
		
		// Prepending 0 to seconds if it is one digit
		if (seconds < 10)
			{
				secondsString = "0" + seconds ;
			} else
			{
				secondsString = "" + seconds ;
			}
		
		finalTimerString = finalTimerString + minutesString + ":" + secondsString ;
		
		// return timer string
		return finalTimerString ;
	}
	
	/**
	 * Function to get Progress percentage
	 * 
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public static int getProgressPercentage(long currentDuration,
			long totalDuration)
	{
		Double percentage = (double) 0 ;
		
		long currentSeconds = (int) (currentDuration / 1000) ;
		long totalSeconds = (int) (totalDuration / 1000) ;
		
		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100 ;
		
		// return percentage
		return percentage.intValue() ;
	}
	
	/**
	 * Function to change progress to timer
	 * 
	 * @param progress
	 *           -
	 * @param totalDuration
	 *           returns current duration in milliseconds
	 * */
	public static int progressToTimer(int progress, int totalDuration)
	{
		int currentDuration = 0 ;
		totalDuration = (int) (totalDuration / 1000) ;
		currentDuration = (int) ((((double) progress) / 100) * totalDuration) ;
		
		// return current duration in milliseconds
		return currentDuration * 1000 ;
	}
	
	
	/**
	 * md5Sum
	 * @Description: Return md5 String from byte array
	 * @param original
	 * @return
	 */
	public static String md5sum(byte[] original) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(original, 0, original.length);
			StringBuffer md5sum = new StringBuffer(new BigInteger(1,
					md.digest()).toString(16));
			while (md5sum.length() < 32)
				md5sum.insert(0, "0");
			return md5sum.toString();
		} catch (NoSuchAlgorithmException e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * md5 converter
	 * @Description: Encrypt a String by MD5
	 * @param original
	 * @return
	 */
	public static String md5sum(String original) {
		return md5sum(original.getBytes());
	}
	
	public static String getIMEI(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static void downloadImage(String url, String output) {
		try {
			InputStream is = (InputStream) fetch(url);
			
			OutputStream outputStream = new FileOutputStream(output);
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			outputStream.close();
			is.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Drawable downloadImage(String url) {
		try {
			InputStream is = (InputStream) fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		URLConnection conn = url.openConnection();
		Object content = conn.getContent();
		return content;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//	public static void cleanAllDataById(MediaItem item, Context context) {
//		if(item==null){
//			DebugLog.e(TAG,"cleanAllDataById eroro: mediaId==null");
//			return;
//		}
//		DataBaseAdapter db = DataBaseAdapter.getInstance(context);
//		boolean i = db.deleteMediaItemById(item.getMediaId());
//		DebugLog.i(TAG,"deleteMediaItemById: "+ i);
//		CommonUtils.deleteFile(item.getLocation());
//	}
	
	/**
	 * 
	 * @Description:set Background for View
	 * @param v
	 * @param colorGetFromDatabase: example : 53ABCE, FFFFFF
	 * @param isGradientDrawble: if background is shape xml
	 */
	
	/** hideKeySoft */
	public static void hideKeySoft(View v, Context context) {
		try {
			IBinder i;
			if (v != null){
				i = v.getWindowToken();
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(i,
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @Description:
	 * @param size
	 *            in kilobyte
	 * @return text covert form kilobyte follow( GB > MB > KB)
	 */
	public static String getTextSize(String size) {
		int sInt = 0;
		try {
			sInt = Integer.parseInt(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sInt != 0) {
			return getSize(sInt);
		} else {
			return "";
		}
	}

	public static String getSize(int strByte) {
		String hrSize = "";
		int b = strByte;
		float k = strByte / 1024F;
		float m = k  	/ 1024F;

		DecimalFormat dec = new DecimalFormat("0.00");
		if (b>1) {
			hrSize = dec.format(b).concat("B");
		}
		if (k > 1) {
			hrSize = dec.format(k).concat("KB");
		}
		
		if (m > 1) {
			hrSize = dec.format(m).concat("MB");
		}
		
		return hrSize;
	}
}
