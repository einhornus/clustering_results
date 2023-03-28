private boolean checkPartial(String index) {
                    // Make sure that index was fully snapshotted
                    if (failed(snapshot, index)) {
                        if (request.partial()) {
                            return true;
                        } else {
                            throw new SnapshotRestoreException(snapshotId, "index [" + index + "] wasn't fully snapshotted - cannot restore");
                        }
                    } else {
                        return false;
                    }
                }
--------------------

@Test
    public void testUngeneratedFieldsThatAreNeverStored() throws IOException {
        String createIndexSource = "{\n" +
                "  \"settings\": {\n" +
                "    \"index.translog.disable_flush\": true,\n" +
                "    \"refresh_interval\": \"-1\"\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"doc\": {\n" +
                "      \"_source\": {\n" +
                "        \"enabled\": \"" + randomBoolean() + "\"\n" +
                "      },\n" +
                "      \"properties\": {\n" +
                "        \"suggest\": {\n" +
                "          \"type\": \"completion\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        assertAcked(prepareCreate("test").addAlias(new Alias("alias")).setSource(createIndexSource));
        ensureGreen();
        String doc = "{\n" +
                "  \"suggest\": {\n" +
                "    \"input\": [\n" +
                "      \"Nevermind\",\n" +
                "      \"Nirvana\"\n" +
                "    ],\n" +
                "    \"output\": \"Nirvana - Nevermind\"\n" +
                "  }\n" +
                "}";

        index("test", "doc", "1", doc);
        String[] fieldsList = {"suggest"};
        // before refresh - document is only in translog
        assertGetFieldsAlwaysNull(indexOrAlias(), "doc", "1", fieldsList);
        refresh();
        //after refresh - document is in translog and also indexed
        assertGetFieldsAlwaysNull(indexOrAlias(), "doc", "1", fieldsList);
        flush();
        //after flush - document is in not anymore translog - only indexed
        assertGetFieldsAlwaysNull(indexOrAlias(), "doc", "1", fieldsList);
    }
--------------------

@Test
    public void testPostingsHighlighterEscapeHtml() throws Exception {
        assertAcked(prepareCreate("test")
                .addMapping("type1", "title", "type=string," + randomStoreField() + "index_options=offsets"));
        ensureYellow();

        IndexRequestBuilder[] indexRequestBuilders = new IndexRequestBuilder[5];
        for (int i = 0; i < 5; i++) {
            indexRequestBuilders[i] = client().prepareIndex("test", "type1", Integer.toString(i))
                    .setSource("title", "This is a html escaping highlighting test for *&? elasticsearch");
        }
        indexRandom(true, indexRequestBuilders);

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(matchQuery("title", "test"))
                .setHighlighterEncoder("html")
                .addHighlightedField("title").get();

        for (int i = 0; i < indexRequestBuilders.length; i++) {
            assertHighlight(searchResponse, i, "title", 0, 1, equalTo("This is a html escaping highlighting <em>test</em> for *&amp;?"));
        }
    }
--------------------

