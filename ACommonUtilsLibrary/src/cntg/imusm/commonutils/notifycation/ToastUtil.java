/**
 
 */
package cntg.imusm.commonutils.notifycation;

import android.content.Context;
import android.widget.Toast;

/**
 * @author  vtthach
 * @CreatedDate Mar 19, 2014
 * @Description: TODO
 */
public class ToastUtil {
	static Toast mToast;
	public static void showToast(String msg, Context context){
		if(mToast == null){
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}
}
