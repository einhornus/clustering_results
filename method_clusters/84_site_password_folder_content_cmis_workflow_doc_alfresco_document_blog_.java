@Test(expectedExceptions = RuntimeException.class)
    public void requestSiteMembershipNoExistentUser()
    {
        userService.requestSiteMembership("fakeUser", "fakePass", globalSite);
    }
--------------------

@Test(expectedExceptions = RuntimeException.class)
    public void addDashletToInvalidSite()
    {
        site.addDashlet(ADMIN, ADMIN, "fakeSite", SiteDashlet.ADDONS_RSS_FEED, DashletLayout.FOUR_COLUMNS, 2, 1);
    }
--------------------

@Test
    public void addFolderComplianceableAspect()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 3);
        Date removeAfter = calendar.getTime();
        contentAspect.addComplianceable(userName, password, siteName, folder, removeAfter);
        List<Property<?>> properties = contentAspect.getProperties(userName, password, siteName, folder);
        Assert.assertTrue(contentAspect.getPropertyValue(properties, "cm:removeAfter").contains(new SimpleDateFormat(DATE_FORMAT).format(removeAfter)));
    }
--------------------

