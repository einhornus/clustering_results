@Test
    @LoadGraphWith(MODERN)
    public void g_V_whereXoutXcreatedX_and_outXknowsX_or_inXknowsXX_valuesXnameX() {
        final Traversal<Vertex, String> traversal = get_g_V_whereXoutXcreatedX_and_outXknowsX_or_inXknowsXX_valuesXnameX();
        printTraversalForm(traversal);
        checkResults(Arrays.asList("marko", "vadas", "josh"), traversal);
    }
--------------------

@Test
    @LoadGraphWith(MODERN)
    public void g_V_aggregateXlocal_aX_byXoutEXcreatedX_countX_out_out_aggregateXlocal_aX_byXinEXcreatedX_weight_sumX() {
        final Traversal<Vertex, Collection> traversal = get_g_V_aggregateXlocal_aX_byXoutEXcreatedX_countX_out_out_aggregateXlocal_aX_byXinEXcreatedX_weight_sumX_capXaX();
        printTraversalForm(traversal);
        assertTrue(traversal.hasNext());
        final Collection store = traversal.next();
        assertFalse(traversal.hasNext());
        assertEquals(8, store.size());
        assertTrue(store.contains(0L));
        assertTrue(store.contains(1L));
        assertTrue(store.contains(2L));
        assertTrue(store.contains(1.0d));
        assertFalse(store.isEmpty());
    }
--------------------

@Test
    @LoadGraphWith(MODERN)
    public void g_VX1X_outXknowsX_outXcreatedX_rangeX0_1X() {
        final Traversal<Vertex, Vertex> traversal = get_g_VX1X_outXknowsX_outXcreatedX_rangeX0_1X(convertToVertexId("marko"));
        printTraversalForm(traversal);
        int counter = 0;
        while (traversal.hasNext()) {
            counter++;
            final String name = traversal.next().value("name");
            assertTrue(name.equals("lop") || name.equals("ripple"));
        }
        assertEquals(1, counter);
    }
--------------------

