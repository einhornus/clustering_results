public void rollback() throws HsqlException {

        resultOut.setResultType(ResultConstants.SQLENDTRAN);

        resultOut.updateCount = ResultConstants.ROLLBACK;

        resultOut.setMainString("");
        execute(resultOut);
    }
--------------------

public static void setAutoCommit (Properties ctx, int WindowNo, boolean autoCommit)
	{
		if (ctx == null)
			return;
		ctx.setProperty(WindowNo+"|AutoCommit", convert(autoCommit));
	}
--------------------

@Override public void validate(String mode) throws SQLException {
                if (!F.isEmpty(mode)) {
                    try {
                        NestedTxMode.valueOf(mode.toUpperCase());
                    }
                    catch (IllegalArgumentException e) {
                        throw new SQLException("Invalid nested transactions handling mode, allowed values: " +
                            Arrays.toString(nestedTxMode.choices), SqlStateCode.CLIENT_CONNECTION_FAILED);
                    }
                }
            }
--------------------

