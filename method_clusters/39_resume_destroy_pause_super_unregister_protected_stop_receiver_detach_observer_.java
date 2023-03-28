@Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(mObserver);
        mScanner.shutdown();
        mUpdateThread.quit();
        if (Constants.LOGVV) {
            Log.v(Constants.TAG, "Service onDestroy");
        }
        super.onDestroy();
    }
--------------------

@Override
	protected void onPause() {
		super.onPause();
		pauseEvent();
		// finish();
	}
--------------------

@Override
    public void dispose() {
        log("Disposing " + this);
        //Unregister for all events
        mParentApp.unregisterForReady(this);
        resetRecords();
        super.dispose();
    }
--------------------

