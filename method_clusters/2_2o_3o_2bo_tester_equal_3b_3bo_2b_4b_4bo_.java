@Test
    public void testAccessOrder3() throws Exception {
        X.println(">>> testAccessOrder3 <<<");

        Map<String, String> map = new LinkedHashMap<>(3, 0.75f, true);

        map.put("k1", "v1");
        map.put("k2", "v2");

        X.println("Initial state: " + map);

        // Accessing first entry.
        map.get("k1");

        X.println("State after get: " + map);
    }
--------------------

public void testCloning() {
        MyComparableObjectSeries s1 = new MyComparableObjectSeries("A");
        s1.add(new Integer(1), "ABC");
        MyComparableObjectSeries s2 = null;
        try {
            s2 = (MyComparableObjectSeries) s1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(s1 != s2);
        assertTrue(s1.getClass() == s2.getClass());
        assertTrue(s1.equals(s2));
    }
--------------------

public void testHashcode() {
        StandardXYBarPainter p1 = new StandardXYBarPainter();
        StandardXYBarPainter p2 = new StandardXYBarPainter();
        assertTrue(p1.equals(p2));
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();
        assertEquals(h1, h2);
    }
--------------------

