public static Intent createInstallIntent() {
        Intent intent = new Intent(ACTION_INSTALL);
        intent.setClassName("com.android.certinstaller",
                            "com.android.certinstaller.CertInstallerMain");
        return intent;
    }
--------------------

@Override
	public boolean isKeyEntry(String alias) throws CMException {
		// Need to make sure we are initialized before we do anything else
		// as Credential Manager can be created but not initialized
		initialize();

		try {
			synchronized (keystore) {
				return keystore.isKeyEntry(alias);
			}
		} catch (Exception ex) {
			String exMessage = "failed to access the key entry in the Keystore";
			logger.error(exMessage, ex);
			throw new CMException(exMessage, ex);
		}
	}
--------------------

@Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // We don't check client certificates
            throw new CertificateException("We don't check client certificates");
        }
--------------------

