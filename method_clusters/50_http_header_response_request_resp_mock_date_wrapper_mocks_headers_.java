private void tryToUpdateVariantMap(final HttpHost target, final HttpRequestWrapper request,
            final Variant matchingVariant) {
        try {
            responseCache.reuseVariantEntryFor(target, request, matchingVariant);
        } catch (final IOException ioe) {
            log.warn("Could not update cache entry to reuse variant", ioe);
        }
    }
--------------------

@Test
    public void testDoesNotReturnStaleResponseIfClientExplicitlyRequestsFirstHandOneWithPragma()
            throws Exception {
        final HttpRequestWrapper req = HttpRequestWrapper.wrap(
                new BasicHttpRequest("GET", "/", HttpVersion.HTTP_1_1));
        req.setHeader("Pragma","no-cache");
        testDoesNotReturnStaleResponseOnError(req);
    }
--------------------

@Test
    public void testSetsResponseInContextOnCacheHit() throws Exception {
        final DummyBackend backend = new DummyBackend();
        final HttpResponse response = HttpTestUtils.make200Response();
        response.setHeader("Cache-Control", "max-age=3600");
        backend.setResponse(response);
        impl = createCachingExecChain(backend, new BasicHttpCache(), CacheConfig.DEFAULT);
        final HttpClientContext ctx = HttpClientContext.create();
        impl.execute(route, request, context, null);
        final HttpResponse result = impl.execute(route, request, ctx, null);
        if (!HttpTestUtils.equivalent(result, ctx.getResponse())) {
            assertSame(result, ctx.getResponse());
        }
    }
--------------------

