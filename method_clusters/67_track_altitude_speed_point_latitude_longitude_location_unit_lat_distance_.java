public double[] getLons() {
            List<GeoPoint> points = getValues();
            double[] lons = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                lons[i] = points.get(i).lon();
            }
            return lons;
        }
--------------------

@Override
	public Map<?, ?> search(double latitude, double longitude,
			int distanceMeters, DateTime fromDate, DateTime toDate) {
		if (fromDate != null && toDate != null) {
			return executeApiRequest(String.format(LAT_LON_DIST_FROM_TO_SCHEME,
					baseUrl, latitude, longitude, distanceMeters, fromDate.toDate().getTime()/1000, toDate.toDate().getTime()/1000, accessToken));
		}
		else if (fromDate == null && toDate != null) {
			return executeApiRequest(String.format(LAT_LON_DIST_TO_SCHEME,
					baseUrl, latitude, longitude, distanceMeters, toDate.toDate().getTime(), accessToken));
		}
		else {
			return executeApiRequest(String.format(LAT_LON_DIST_FROM_SCHEME,
					baseUrl, latitude, longitude, distanceMeters, fromDate.toDate().getTime(), accessToken));
		}
	}
--------------------

private static boolean fieldLooksLikeTimestamp(String inValue, boolean inIsHeader)
	{
		if (inValue == null || inValue.equals("")) {return false;}
		if (inIsHeader)
		{
			String upperValue = inValue.toUpperCase();
			// This is a header line so look for english or local text
			return (upperValue.equals("TIMESTAMP")
				|| upperValue.equals("TIME")
				|| upperValue.equals(I18nManager.getText("fieldname.timestamp").toUpperCase()));
		}
		else
		{
			// must be at least 7 characters long
			if (inValue.length() < 7) {return false;}
			TimestampUtc stamp = new TimestampUtc(inValue);
			return stamp.isValid();
		}
	}
--------------------

