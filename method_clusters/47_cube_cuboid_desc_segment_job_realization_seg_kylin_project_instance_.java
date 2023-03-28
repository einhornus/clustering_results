public boolean buildCube(String cubeName, long startTime, long endTime, String buildType) throws Exception {
        String url = baseUrl + CUBES + cubeName + "/build";
        HttpPut put = newPut(url);
        HttpResponse response = null;
        try {
            HashMap<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("startTime", startTime + "");
            paraMap.put("endTime", endTime + "");
            paraMap.put("buildType", buildType);
            String jsonMsg = new ObjectMapper().writeValueAsString(paraMap);
            put.setEntity(new StringEntity(jsonMsg, UTF_8));
            response = client.execute(put);
            getContent(response);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException(INVALID_RESPONSE + response.getStatusLine().getStatusCode()
                        + " with build cube url " + url + "\n" + jsonMsg);
            } else {
                return true;
            }
        } finally {
            cleanup(put, response);
        }
    }
--------------------

public static void saveCuboidShards(CubeSegment segment, Map<Long, Short> cuboidShards, int totalShards) throws IOException {
        CubeManager cubeManager = CubeManager.getInstance(segment.getConfig());

        Map<Long, Short> filtered = Maps.newHashMap();
        for (Map.Entry<Long, Short> entry : cuboidShards.entrySet()) {
            if (entry.getValue() > 1) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }
        
        // work on copy instead of cached objects
        CubeInstance cubeCopy = segment.getCubeInstance().latestCopyForWrite();
        CubeSegment segCopy = cubeCopy.getSegmentById(segment.getUuid());

        segCopy.setCuboidShardNums(filtered);
        segCopy.setTotalShards(totalShards);

        CubeUpdate update = new CubeUpdate(cubeCopy);
        update.setToUpdateSegs(segCopy);
        cubeManager.updateCube(update);
    }
--------------------

public List<CubeInstance> listAllCubes() {
        try (AutoLock lock = cubeMapLock.lockForRead()) {
            return new ArrayList<CubeInstance>(cubeMap.values());
        }
    }
--------------------

