public static ConnectivityController get(JobSchedulerService jms) {
        synchronized (sCreationLock) {
            if (mSingleton == null) {
                mSingleton = new ConnectivityController(jms, jms.getContext());
            }
            return mSingleton;
        }
    }
--------------------

public static Code getInstance() {
        if(bmpCode == null)
            bmpCode = new Code();
        return bmpCode;
    }
--------------------

public void testSingleton() {
        BitmapManager manager = BitmapManager.instance();
        assertNotNull(manager);
        assertNotNull(mBitmapManager);
        assertSame(manager, mBitmapManager);
    }
--------------------

