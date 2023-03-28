@Override
    @ServiceThreadOnly
    protected void onStandby(boolean initiatedByCec, int standbyAction) {
        assertRunOnServiceThread();
        if (!mService.isControlEnabled() || initiatedByCec) {
            return;
        }
        switch (standbyAction) {
            case HdmiControlService.STANDBY_SCREEN_OFF:
                if (mAutoTvOff) {
                    mService.sendCecCommand(
                            HdmiCecMessageBuilder.buildStandby(mAddress, Constants.ADDR_TV));
                }
                break;
            case HdmiControlService.STANDBY_SHUTDOWN:
                // ACTION_SHUTDOWN is taken as a signal to power off all the devices.
                mService.sendCecCommand(
                        HdmiCecMessageBuilder.buildStandby(mAddress, Constants.ADDR_BROADCAST));
                break;
        }
    }
--------------------

@Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("port_id: ").append(mId).append(", ");
        s.append("address: ").append(String.format("0x%04x", mAddress)).append(", ");
        s.append("cec: ").append(mCecSupported).append(", ");
        s.append("arc: ").append(mArcSupported).append(", ");
        s.append("mhl: ").append(mMhlSupported);
        return s.toString();
    }
--------------------

private void handleVendorId(HdmiCecMessage cmd) {
        Preconditions.checkState(mProcessedDeviceCount < mDevices.size());

        DeviceInfo current = mDevices.get(mProcessedDeviceCount);
        if (current.mLogicalAddress != cmd.getSource()) {
            Slog.w(TAG, "Unmatched address[expected:" + current.mLogicalAddress + ", actual:" +
                    cmd.getSource());
            return;
        }

        byte[] params = cmd.getParams();
        int vendorId = HdmiUtils.threeBytesToInt(params);
        current.mVendorId = vendorId;
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }
--------------------

