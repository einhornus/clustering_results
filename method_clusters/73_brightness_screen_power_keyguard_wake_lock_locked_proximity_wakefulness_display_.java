protected void dumpOnHandler(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print(TAG + ": ");
        if (mWindowToken == null) {
            pw.println("stopped");
        } else {
            pw.println("running (token=" + mWindowToken + ")");
        }
        pw.println("  window: " + mWindow);
        pw.print("  flags:");
        if (isInteractive()) pw.print(" interactive");
        if (isLowProfile()) pw.print(" lowprofile");
        if (isFullscreen()) pw.print(" fullscreen");
        if (isScreenBright()) pw.print(" bright");
        if (isWindowless()) pw.print(" windowless");
        if (isDozing()) pw.print(" dozing");
        else if (canDoze()) pw.print(" candoze");
        pw.println();
        if (canDoze()) {
            pw.println("  doze screen state: " + Display.stateToString(mDozeScreenState));
            pw.println("  doze screen brightness: " + mDozeScreenBrightness);
        }
    }
--------------------

public void setScreenState(int state) {
        if (mScreenState != state) {
            if (DEBUG) {
                Slog.d(TAG, "setScreenState: state=" + state);
            }

            mScreenState = state;
            mScreenReady = false;
            scheduleScreenUpdate();
        }
    }
--------------------

private void updateSettingsLocked() {
        final ContentResolver resolver = mContext.getContentResolver();

        mDreamsEnabledSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ENABLED,
                mDreamsEnabledByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mDreamsActivateOnSleepSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ACTIVATE_ON_SLEEP,
                mDreamsActivatedOnSleepByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mDreamsActivateOnDockSetting = (Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SCREENSAVER_ACTIVATE_ON_DOCK,
                mDreamsActivatedOnDockByDefaultConfig ? 1 : 0,
                UserHandle.USER_CURRENT) != 0);
        mScreenOffTimeoutSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_OFF_TIMEOUT, DEFAULT_SCREEN_OFF_TIMEOUT,
                UserHandle.USER_CURRENT);
        mSleepTimeoutSetting = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.SLEEP_TIMEOUT, DEFAULT_SLEEP_TIMEOUT,
                UserHandle.USER_CURRENT);
        mStayOnWhilePluggedInSetting = Settings.Global.getInt(resolver,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN, BatteryManager.BATTERY_PLUGGED_AC);
        mTheaterModeEnabled = Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.THEATER_MODE_ON, 0) == 1;

        if (mSupportsDoubleTapWakeConfig) {
            boolean doubleTapWakeEnabled = Settings.Secure.getIntForUser(resolver,
                    Settings.Secure.DOUBLE_TAP_TO_WAKE, DEFAULT_DOUBLE_TAP_TO_WAKE,
                            UserHandle.USER_CURRENT) != 0;
            if (doubleTapWakeEnabled != mDoubleTapWakeEnabled) {
                mDoubleTapWakeEnabled = doubleTapWakeEnabled;
                nativeSetFeature(POWER_FEATURE_DOUBLE_TAP_TO_WAKE, mDoubleTapWakeEnabled ? 1 : 0);
            }
        }

        final int oldScreenBrightnessSetting = mScreenBrightnessSetting;
        mScreenBrightnessSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_BRIGHTNESS, mScreenBrightnessSettingDefault,
                UserHandle.USER_CURRENT);
        if (oldScreenBrightnessSetting != mScreenBrightnessSetting) {
            mTemporaryScreenBrightnessSettingOverride = -1;
        }

        final float oldScreenAutoBrightnessAdjustmentSetting =
                mScreenAutoBrightnessAdjustmentSetting;
        mScreenAutoBrightnessAdjustmentSetting = Settings.System.getFloatForUser(resolver,
                Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, 0.0f,
                UserHandle.USER_CURRENT);
        if (oldScreenAutoBrightnessAdjustmentSetting != mScreenAutoBrightnessAdjustmentSetting) {
            mTemporaryScreenAutoBrightnessAdjustmentSettingOverride = Float.NaN;
        }

        mScreenBrightnessModeSetting = Settings.System.getIntForUser(resolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL, UserHandle.USER_CURRENT);

        final boolean lowPowerModeEnabled = Settings.Global.getInt(resolver,
                Settings.Global.LOW_POWER_MODE, 0) != 0;
        final boolean autoLowPowerModeConfigured = Settings.Global.getInt(resolver,
                Settings.Global.LOW_POWER_MODE_TRIGGER_LEVEL, 0) != 0;
        if (lowPowerModeEnabled != mLowPowerModeSetting
                || autoLowPowerModeConfigured != mAutoLowPowerModeConfigured) {
            mLowPowerModeSetting = lowPowerModeEnabled;
            mAutoLowPowerModeConfigured = autoLowPowerModeConfigured;
            updateLowPowerModeLocked();
        }

        mDirty |= DIRTY_SETTINGS;
    }
--------------------

