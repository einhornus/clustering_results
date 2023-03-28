private QueryStmt buildQueryStmt(ASTNode tree) throws SemanticException {

    StatementBase stmt = buildStmt(tree);

    if (stmt instanceof InsertStmt)
      return ((InsertStmt) stmt).getQueryStmt();
    else
      // WARNING: This cast is dangerous, should guarantee buildStmt return a
      // QueryStmt.
      return (QueryStmt) stmt;
  }
--------------------

int xlateRSHoldability(int holdability) throws SQLException {

        SQLWarning w;
        String     msg;

        switch (holdability) {

            case jdbcResultSet.HOLD_CURSORS_OVER_COMMIT : {
                return holdability;
            }
            case jdbcResultSet.CLOSE_CURSORS_AT_COMMIT : {
                msg = "CLOSE_CURSORS_AT_COMMIT => HOLD_CURSORS_OVER_COMMIT";
                w = new SQLWarning(msg, "SOO10", Trace.INVALID_JDBC_ARGUMENT);

                addWarning(w);

                return jdbcResultSet.HOLD_CURSORS_OVER_COMMIT;
            }
            default : {
                msg = "ResultSet holdability: " + holdability;

                throw Util.sqlException(Trace.INVALID_JDBC_ARGUMENT, msg);
            }
        }
    }
--------------------

private static boolean isCacheable(int statementType) {
        if (statementType == DatabaseUtils.STATEMENT_UPDATE
                || statementType == DatabaseUtils.STATEMENT_SELECT) {
            return true;
        }
        return false;
    }
--------------------

