private Iterable<Datapoint> getDatapoints(String itemName, GroupAddress groupAddress) {
		for (KNXBindingProvider provider : providers) {
			Iterable<Datapoint> datapoints = provider.getDatapoints(itemName, groupAddress);
			if (datapoints != null)
				return datapoints;
		}
		return null;
	}
--------------------

private Configuration getConfiguration(ConfigAdminBindingConfig bindingConfig) {
		Configuration result = null;
		if (bindingConfig != null) {
			try {
				result = configAdmin.getConfiguration(bindingConfig.normalizedPid);
			} catch (IOException ioe) {
				logger.warn("Fetching configuration for pid '" + bindingConfig.normalizedPid + "' failed", ioe);
			}
		}
		return result;
	}
--------------------

@Override
        public Bundle getCarrierConfigValues(int subId) throws RemoteException {
            Slog.d(TAG, "getCarrierConfigValues() by " + getCallingPackageName());
            return getServiceGuarded().getCarrierConfigValues(subId);
        }
--------------------

