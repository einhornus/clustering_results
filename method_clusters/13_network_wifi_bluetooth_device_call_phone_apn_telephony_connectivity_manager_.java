public int getType() {
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device type");
            return DEVICE_TYPE_UNKNOWN;
        }
        try {
            return sService.getRemoteType(this);
        } catch (RemoteException e) {Log.e(TAG, "", e);}
        return DEVICE_TYPE_UNKNOWN;
    }
--------------------

@Override
    public void onCreate() {
        if (DEBUG) Log.d(TAG, "onCreate");

        LocalBluetoothManager manager = LocalBluetoothManager.getInstance(this);
        if (manager == null) {
            Log.e(TAG, "Can't get LocalBluetoothManager: exiting");
            return;
        }

        mLocalAdapter = manager.getBluetoothAdapter();
        mDeviceManager = manager.getCachedDeviceManager();
        mProfileManager = manager.getProfileManager();
        if (mProfileManager == null) {
            Log.e(TAG, "Can't get LocalBluetoothProfileManager: exiting");
            return;
        }

        HandlerThread thread = new HandlerThread("DockService");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }
--------------------

public boolean processMessage(Message message) {
            switch (message.what) {
                case WifiStateMachine.CMD_SET_AP_CONFIG:
                case WifiStateMachine.CMD_SET_AP_CONFIG_COMPLETED:
                    Log.e(TAG, "Unexpected message: " + message);
                    break;
                case WifiStateMachine.CMD_REQUEST_AP_CONFIG:
                    mReplyChannel.replyToMessage(message,
                            WifiStateMachine.CMD_RESPONSE_AP_CONFIG, mWifiApConfig);
                    break;
                default:
                    Log.e(TAG, "Failed to handle " + message);
                    break;
            }
            return HANDLED;
        }
--------------------

