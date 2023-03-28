private void updateVendorLinks(DipVendorId dipVendorId,
			DipFormatId dipFormatId, List<String> exclusionsList)
			throws FfmaCommonException {
		List<? extends FfmaDomainObject> dipFormats = preservationRiskmanagementDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		Iterator<? extends FfmaDomainObject> iter = dipFormats.iterator();
		while (iter.hasNext()) {
			DipFormatId dipFormatIdEntry = (DipFormatId) iter.next();
			dipFormatIdEntry.setDipVendorId(LODStatisticsUtils
					.addValueToStringArray(
							dipFormatIdEntry.getDipVendorId(),
							dipVendorId.getDipId()));
			preservationRiskmanagementDao.updateObject(dipFormatId, dipFormatIdEntry);
		}
	}
--------------------

public static List<String> searchSoftwareReleasedAfter(String date) {
		List<String> resultList = new ArrayList<String>();
		RepositoryDescription dbpedia = new DBPediaConnector();

		RiskProperty riskProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl connector = new ConnectorImpl(riskProperty);

		dbpedia.setConnector(connector);
		List<Map<String, String>> dbpediaList = dbpedia.retrieveAll();

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			String value = dbpediaMap.get(RiskConstants.COLUMN_RELEASED);
			if (value != null && value.compareTo(date) > 0) {
				if (!resultList.contains(dbpediaMap.get(RiskConstants.COLUMN_NAME))) {
					resultList.add(dbpediaMap.get(RiskConstants.COLUMN_NAME));
				}
			}
		}
		return resultList;
	}
--------------------

public static void setFreebaseLodSoftwareDescriptionAndId(
			String softwareId, LODSoftware lodSoftware) {
		List<String> guidList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_GUID, LODConstants.FB_ID);

		List<String> descriptions = new ArrayList<String>();
		String guidStr = ReportConstants.EMPTYSTRING;
		Iterator<String> guidIter = guidList.iterator();
		while (guidIter.hasNext()) {
			guidStr = guidIter.next();
			String descriptionStr = LODUtils.getFreebaseFormatDescription(guidStr);
			descriptions.add(descriptionStr);
			break;
		}

		if (descriptions.size() > 0) {
			lodSoftware.setDescription(descriptions.get(0));
		}
		lodSoftware.setRepositoryId(softwareId);
	}
--------------------

