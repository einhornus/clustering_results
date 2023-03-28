private void checkRebalancingDuringLoad(
        int restartDelay,
        int checkpointDelay,
        int threads,
        final int batch
    ) throws Exception {
        this.checkpointDelay = checkpointDelay;

        startGrids(GRID_CNT);

        final IgniteEx load = ignite(0);

        load.cluster().active(true);

        try (IgniteDataStreamer<Object, Object> s = load.dataStreamer(CACHE_NAME)) {
            s.allowOverwrite(true);

            for (int i = 0; i < ENTRIES_COUNT; i++)
                s.addData(i, i);
        }

        final AtomicBoolean done = new AtomicBoolean(false);

        IgniteInternalFuture<?> busyFut = GridTestUtils.runMultiThreadedAsync(new Callable<Object>() {
            /** {@inheritDoc} */
            @Override public Object call() throws Exception {
                IgniteCache<Object, Object> cache = load.cache(CACHE_NAME);
                Random rnd = ThreadLocalRandom.current();

                while (!done.get()) {
                    final int mode = rnd.nextInt(3);

                    Map<Integer, Person> map = new TreeMap<>();

                    for (int i = 0; i < batch; i++) {
                        int key = rnd.nextInt(ENTRIES_COUNT);

                        map.put(key, new Person("fn" + key, "ln" + key));
                    }

                    while (true) {
                        try {
                            switch (mode) {
                                case 0: // Pessimistic tx.
                                    try (Transaction tx = load.transactions().txStart(PESSIMISTIC, READ_COMMITTED)) {
                                        cache.putAll(map);

                                        tx.commit();
                                    }

                                    break;

                                case 1: // Optimistic serializable tx.
                                    try (Transaction tx = load.transactions().txStart(OPTIMISTIC, SERIALIZABLE)) {
                                        cache.putAll(map);

                                        tx.commit();
                                    }

                                    break;

                                default: // Implicit tx.
                                    cache.putAll(map);
                            }

                            break;
                        }
                        catch (Exception e) {
                            if (X.hasCause(e,
                                TransactionOptimisticException.class,
                                TransactionRollbackException.class,
                                ClusterTopologyException.class,
                                NodeStoppingException.class))
                                continue; // Expected types.

                            MvccFeatureChecker.assertMvccWriteConflict(e);
                        }
                    }
                }

                return null;
            }
        }, threads, "updater");

        long end = System.currentTimeMillis() + SF.apply(90000);

        Random rnd = ThreadLocalRandom.current();

        while (System.currentTimeMillis() < end) {
            int idx = rnd.nextInt(2) + 1; // Prevent data loss.

            stopGrid(idx, cancel);

            U.sleep(restartDelay);

            startGrid(idx);

            U.sleep(restartDelay);
        }

        done.set(true);

        busyFut.get();

        awaitPartitionMapExchange();

        // Skip consistency check if expiration is on.
        if (validatePartitions())
            assertPartitionsSame(idleVerify(load, CACHE_NAME));
    }
--------------------

@Override public Object call() throws Exception {
                        int nodeId = nodeIdx.getAndIncrement();

                        IgniteCache<Integer, Integer> cache = grid(nodeId).cache(DEFAULT_CACHE_NAME);

                        int cntr = 0;

                        while (!done.get()) {
                            int part = ThreadLocalRandom.current().nextInt(ignite(nodeId).affinity(DEFAULT_CACHE_NAME).partitions());

                            if (cntr++ % 100 == 0)
                                info("Running query [node=" + nodeId + ", part=" + part + ']');

                            try (QueryCursor<Cache.Entry<Integer, Integer>> cur =
                                     cache.query(new ScanQuery<Integer, Integer>(part).setPageSize(5))) {

                                doTestScanQueryCursor(cur, part);
                            }
                        }

                        return null;
                    }
--------------------

private void processNearLockRequest(UUID nodeId, GridNearLockRequest req) {
        assert ctx.affinityNode();
        assert nodeId != null;
        assert req != null;

        if (txLockMsgLog.isDebugEnabled()) {
            txLockMsgLog.debug("Received near lock request [txId=" + req.version() +
                ", inTx=" + req.inTx() +
                ", node=" + nodeId + ']');
        }

        ClusterNode nearNode = ctx.discovery().node(nodeId);

        if (nearNode == null) {
            U.warn(txLockMsgLog, "Received near lock request from unknown node (will ignore) [txId=" + req.version() +
                ", inTx=" + req.inTx() +
                ", node=" + nodeId + ']');

            return;
        }

        processNearLockRequest0(nearNode, req);
    }
--------------------

