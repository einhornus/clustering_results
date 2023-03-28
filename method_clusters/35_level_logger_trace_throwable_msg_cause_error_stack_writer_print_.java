static public int vString(String tag, String msg) {
		if (log)
			return Log.v(tag, msg);
		else
			return 0;
	}
--------------------

public static void e(String tag, String msg, Throwable tr) {
        if( ! isDebuggable() || msg == null)
            return;
        Log.e(tag, msg, tr);
    }
--------------------

private void logWarn(String msg) {
    if (log.isWarnEnabled()) {
      log.warn(logPrefix + msg);
    }
  }
--------------------

