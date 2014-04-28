package cntg.imusm.commonutils.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import cntg.imusm.commonutils.debug.MyDebugLog;

public class GraphicsUtils {
	private static final String TAG = "GraphicsUtils";

	/**
	 * @Description:set Background for View
	 * @param v
	 * @param colorCode : example : 53ABCE, FFFFFF
	 * @param isGradientDrawble : if background is shape XML
	 */
	public static void setBackgroundView(View v, String colorCode,
			boolean isGradientDrawble) {
		if (v == null || colorCode == null) {
			MyDebugLog.e(TAG, "setBackgroundView error, check here");
			return;
		}
		int color = -1;
		try {
			color = Color.parseColor("#" + colorCode);
		} catch (IllegalArgumentException e) {
			MyDebugLog.e(TAG, "colorGetFromDatabase wrong format:" + colorCode);
			color = -1;
		}

		if (color != -1) {
			if (isGradientDrawble) {
				GradientDrawable gradientDrawable = (GradientDrawable) v
						.getBackground();
				if (gradientDrawable != null) {
					gradientDrawable.setColor(color);
				}
			} else {
				v.setBackgroundColor(color);
			}
		} else {
			MyDebugLog.e(TAG, "colorGetFromDatabase wrong format:" + colorCode);
		}
	}

	public static void setBackgroundView(View v, int colorCode,
			boolean isGradientDrawble) {
		if (v == null) {
			MyDebugLog.e(TAG, "setBackgroundView error, check here");
			return;
		}
		if (isGradientDrawble) {
			GradientDrawable gradientDrawable = (GradientDrawable) v
					.getBackground();
			if (gradientDrawable != null) {
				gradientDrawable.setColor(colorCode);
			}
		} else {
			v.setBackgroundColor(colorCode);
		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
