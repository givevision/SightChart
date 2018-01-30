package com.givevision.sightchart;

public class LogManagement {
	// logging facilities to enable easy overriding. thanks, Dan!
		//
	public static Boolean D = true; 
	
	public static void Log_v(String tag, String message) {
		if (!LoggerConfig.ON)
			return;
		Log_v(tag, message, null);
	}

	protected static void Log_v(String tag, String message, Throwable e) {
		if (!LoggerConfig.ON)
			return;
		log("v", tag, message, e);
	}

	public static void Log_d(String tag, String message) {
		if (!LoggerConfig.ON)
			return;
		Log_d(tag, message, null);
	}

	protected static void Log_d(String tag, String message, Throwable e) {
		if (!LoggerConfig.ON)
			return;
		log("d", tag, message, e);
	}

	public static void Log_i(String tag, String message) {
		if (!LoggerConfig.ON)
			return;
		Log_i(tag, message, null);
	}

	protected static void Log_i(String tag, String message, Throwable e) {
		if (!LoggerConfig.ON)
			return;
		log("i", tag, message, e);
	}

	public static void Log_w(String tag, String message) {
		if (!LoggerConfig.ON)
			return;
		Log_w(tag, message, null);
	}

	protected static void Log_w(String tag, String message, Throwable e) {
		if (!LoggerConfig.ON)
			return;
		log("w", tag, message, e);
	}

	public static void Log_e(String tag, String message) {
		if (!LoggerConfig.ON)
			return;
		Log_e(tag, message, null);
	}

	protected static void Log_e(String tag, String message, Throwable e) {
		if (!LoggerConfig.ON)
			return;
		log("e", tag, message, e);
	}

	protected static void log(String level, String tag, String message, Throwable e) {
			if (message == null || D==false) {
				return;
			}
			if (level.equalsIgnoreCase("v")) {
				if (e == null)
					android.util.Log.v(tag, message);
				else
					android.util.Log.v(tag, message, e);
			} else if (level.equalsIgnoreCase("d")) {
				if (e == null)
					android.util.Log.d(tag, message);
				else
					android.util.Log.d(tag, message, e);
			} else if (level.equalsIgnoreCase("i")) {
				if (e == null)
					android.util.Log.i(tag, message);
				else
					android.util.Log.i(tag, message, e);
			} else if (level.equalsIgnoreCase("w")) {
				if (e == null)
					android.util.Log.w(tag, message);
				else
					android.util.Log.w(tag, message, e);
			} else {
				if (e == null)
					android.util.Log.e(tag, message);
				else
					android.util.Log.e(tag, message, e);
			}
	}
}
