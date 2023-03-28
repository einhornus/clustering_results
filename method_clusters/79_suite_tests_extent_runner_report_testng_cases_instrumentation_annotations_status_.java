public final TestSuite build() {
        rootSuite = new TestSuite(getSuiteName());

        // Keep track of current class so we know when to create a new sub-suite.
        currentClassname = null;
        try {
            for (TestMethod test : testGrouping.getTests()) {
                if (satisfiesAllPredicates(test)) {
                    addTest(test);
                }
            }
            if (testCases.size() > 0) {
                for (TestCase testCase : testCases) {
                    if (satisfiesAllPredicates(new TestMethod(testCase))) {
                        addTest(testCase);
                    }
                }
            }
        } catch (Exception exception) {
            Log.i("TestSuiteBuilder", "Failed to create test.", exception);
            TestSuite suite = new TestSuite(getSuiteName());
            suite.addTest(new FailedToCreateTests(exception));
            return suite;
        }
        return rootSuite;
    }
--------------------

@Test
    public void testBuildRequest_multiClass() {
        Bundle b = new Bundle();
        b.putString(AndroidJUnitRunner.ARGUMENT_TEST_CLASS, "ClassName1,ClassName2");
        mAndroidJUnitRunner.buildRequest(b, mStubStream);
        Mockito.verify(mMockBuilder).addTestClass("ClassName1");
        Mockito.verify(mMockBuilder).addTestClass("ClassName2");
    }
--------------------

protected void runInAllDataModes(TestRunnable call, DataMode... dataModes) throws Exception {
        if (F.isEmpty(dataModes))
            dataModes = DataMode.values();

        for (int i = 0; i < dataModes.length; i++) {
            dataMode = dataModes[i];

            if (!isCompatible()) {
                info("Skipping test in data mode: " + dataMode);

                continue;
            }

            info("Running test in data mode: " + dataMode);

            if (i != 0)
                beforeTest();

            try {
                call.run();
            }
            catch (Throwable e) {
                e.printStackTrace();

                throw e;
            }
            finally {
                if (i + 1 != DataMode.values().length)
                    afterTest();
            }
        }
    }
--------------------

