protected ObjectNode getClusterStateJSON(IServletRequest request, String pathToNode) {
        ObjectNode json = appCtx.getClusterStateManager().getClusterStateDescription();
        ICcApplicationContext appConfig = (ICcApplicationContext) ctx.get(ASTERIX_APP_CONTEXT_INFO_ATTR);
        json.putPOJO("config", ConfigUtils.getSectionOptionsForJSON(appConfig.getServiceContext().getAppConfig(),
                Section.COMMON, getConfigSelector()));

        ArrayNode ncs = (ArrayNode) json.get("ncs");
        String clusterURL = resolveClusterUrl(request, pathToNode);
        String adminURL = HttpUtil.canonicalize(clusterURL + "../");
        String nodeURL = clusterURL + "node/";
        for (int i = 0; i < ncs.size(); i++) {
            ObjectNode nc = (ObjectNode) ncs.get(i);
            nc.put(CONFIG_URI_KEY, nodeURL + nc.get(NODE_ID_KEY).asText() + "/config");
            nc.put(STATS_URI_KEY, nodeURL + nc.get(NODE_ID_KEY).asText() + "/stats");
            nc.put(THREAD_DUMP_URI_KEY, nodeURL + nc.get(NODE_ID_KEY).asText() + "/threaddump");
        }
        ObjectNode cc;
        if (json.has("cc")) {
            cc = (ObjectNode) json.get("cc");
        } else {
            cc = OBJECT_MAPPER.createObjectNode();
            json.set("cc", cc);
        }
        cc.put(CONFIG_URI_KEY, clusterURL + "cc/config");
        cc.put(STATS_URI_KEY, clusterURL + "cc/stats");
        cc.put(THREAD_DUMP_URI_KEY, clusterURL + "cc/threaddump");
        json.put(SHUTDOWN_URI_KEY, adminURL + "shutdown");
        json.put(FULL_SHUTDOWN_URI_KEY, adminURL + "shutdown?all=true");
        json.put(VERSION_URI_KEY, adminURL + "version");
        json.put(DIAGNOSTICS_URI_KEY, adminURL + "diagnostics");
        return json;
    }
--------------------

public NodeHeartbeatFunction(String nodeId, HeartbeatData hbData, InetSocketAddress ncAddress) {
            this.nodeId = nodeId;
            this.hbData = hbData;
            this.ncAddress = ncAddress;
        }
--------------------

@Override
    public DeployedJobSpecId deployJobSpec(byte[] acggfBytes) throws Exception {
        HyracksClientInterfaceFunctions.DeployJobSpecFunction sjf =
                new HyracksClientInterfaceFunctions.DeployJobSpecFunction(acggfBytes);
        return (DeployedJobSpecId) rpci.call(ipcHandle, sjf);
    }
--------------------

