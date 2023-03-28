@Override
            public Integer run(ITextToSpeechService service) throws RemoteException {
                if (loc == null) {
                    return LANG_NOT_SUPPORTED;
                }
                String language = null, country = null;
                try {
                    language = loc.getISO3Language();
                } catch (MissingResourceException e) {
                    Log.w(TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + loc, e);
                    return LANG_NOT_SUPPORTED;
                }

                try {
                    country = loc.getISO3Country();
                } catch (MissingResourceException e) {
                    Log.w(TAG, "Couldn't retrieve ISO 3166 country code for locale: " + loc, e);
                    return LANG_NOT_SUPPORTED;
                }

                String variant = loc.getVariant();

                // Check if the language, country, variant are available, and cache
                // the available parts.
                // Note that the language is not actually set here, instead it is cached so it
                // will be associated with all upcoming utterances.

                int result = service.loadLanguage(getCallerIdentity(), language, country, variant);
                if (result >= LANG_AVAILABLE){
                    if (result < LANG_COUNTRY_VAR_AVAILABLE) {
                        variant = "";
                        if (result < LANG_COUNTRY_AVAILABLE) {
                            country = "";
                        }
                    }
                    mParams.putString(Engine.KEY_PARAM_LANGUAGE, language);
                    mParams.putString(Engine.KEY_PARAM_COUNTRY, country);
                    mParams.putString(Engine.KEY_PARAM_VARIANT, variant);
                }
                return result;
            }
--------------------

private static void addLocaleDisplayNameToList(final Context context,
            final ArrayList<LocaleRenderer> list, final String locale) {
        if (null != locale) {
            list.add(new LocaleRenderer(context, locale));
        }
    }
--------------------

@Test
	public void languageChangedTest() {
		mainPresenter.languageChanged(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, UI.getCurrent().getLocale());
		mainPresenter.languageChanged(Locale.ENGLISH);
		Assert.assertEquals(Locale.ENGLISH, UI.getCurrent().getLocale());
	}
--------------------

