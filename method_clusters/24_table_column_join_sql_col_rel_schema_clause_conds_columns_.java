public final void initializeMaps() {
		mapComments.put("description", "A description of the column");
		mapComments.put("name", "The name of the column within the table");
		mapComments.put("table", "The name of the table");
		mapComments.put("type", "The data type of the column (int, float, string, list)");
	}
--------------------

void createPrimaryIndex(int[] pkcols,
                            HsqlName name) throws HsqlException {

        int[] pkcoltypes = new int[pkcols.length];

        for (int j = 0; j < pkcols.length; j++) {
            pkcoltypes[j] = colTypes[pkcols[j]];
        }

        Index newindex = new Index(database, name, this, pkcols, pkcoltypes,
                                   true, true, true, false, pkcols,
                                   pkcoltypes, isTemp);

        addIndex(newindex);
    }
--------------------

@Override
    public String toString() {
        if (isInnerColumn() && parserDescription != null)
            return parserDescription;

        String alias = table == null ? "UNKNOWN_MODEL" : table.getAlias();
        String tableName = column.getTable() == null ? "NULL" : column.getTable().getName();
        String tableIdentity = column.getTable() == null ? "NULL" : column.getTable().getIdentity();
        if (alias.equals(tableName)) {
            return tableIdentity + "." + column.getName();
        } else {
            return alias + ":" + tableIdentity + "." + column.getName();
        }
    }
--------------------

