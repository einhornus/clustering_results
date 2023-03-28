public CreateFeedPolicyStatement(String policyName, String sourcePolicyName, Map<String, String> properties,
            String description, boolean ifNotExists) {
        this.policyName = policyName;
        this.sourcePolicyName = sourcePolicyName;
        this.properties = properties;
        this.description = description;
        this.ifNotExists = ifNotExists;
        sourcePolicyFile = null;
    }
--------------------

@Override
    public List<Function> getDataverseFunctions(MetadataTransactionContext ctx, DataverseName dataverseName)
            throws AlgebricksException {
        try {
            return metadataNode.getDataverseFunctions(ctx.getTxnId(), dataverseName);
        } catch (RemoteException e) {
            throw new MetadataException(ErrorCode.REMOTE_EXCEPTION_WHEN_CALLING_METADATA_NODE, e);
        }
    }
--------------------

public void handleCreateFullTextFilterStatement(MetadataProvider metadataProvider, Statement stmt)
            throws Exception {
        CreateFullTextFilterStatement stmtCreateFilter = (CreateFullTextFilterStatement) stmt;
        String fullTextFilterName = stmtCreateFilter.getFilterName();
        metadataProvider.validateDatabaseObjectName(stmtCreateFilter.getDataverseName(), fullTextFilterName,
                stmt.getSourceLocation());
        DataverseName dataverseName = getActiveDataverseName(stmtCreateFilter.getDataverseName());

        if (isCompileOnly()) {
            return;
        }
        lockUtil.createFullTextFilterBegin(lockManager, metadataProvider.getLocks(), dataverseName, fullTextFilterName);
        try {
            doCreateFullTextFilter(metadataProvider, stmtCreateFilter, dataverseName);
        } finally {
            metadataProvider.getLocks().unlock();
        }
    }
--------------------

