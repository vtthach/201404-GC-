package cntg.imusm.commonutils.debug;

public class MyDebugLog {
	public static final boolean PERFORMANCE_CHECKING = true;
	public static boolean ENABLED = true;

	public static void w(String tag, String string) {
		if (ENABLED)
			android.util.Log.w(tag, string);
	}

	public static void e(String tag, String string, Throwable t) {
		if (ENABLED)
			android.util.Log.e(tag, string, t);
	}

	public static void d(String tag, String string) {
		if (ENABLED)
			android.util.Log.d(tag, string);
	}

	public static void v(String tag, String string) {
		if (ENABLED)
			android.util.Log.v(tag, string);
	}

	public static void i(String tag, String string) {
		if (ENABLED)
			android.util.Log.i(tag, string);
	}

	public static void e(String tag, String string) {
		if (ENABLED)
			android.util.Log.e(tag, string);
	}

	public static void w(String tag, String string, Throwable e) {
		if (ENABLED)
			android.util.Log.w(tag, string, e);
	}
}

