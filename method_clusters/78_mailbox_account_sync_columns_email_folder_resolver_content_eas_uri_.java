private static int copyAccountTables(SQLiteDatabase fromDatabase, SQLiteDatabase toDatabase) {
        if (fromDatabase == null || toDatabase == null) return -1;

        // Lock both databases; for the "from" database, we don't want anyone changing it from
        // under us; for the "to" database, we want to make the operation atomic
        int copyCount = 0;
        fromDatabase.beginTransaction();
        try {
            toDatabase.beginTransaction();
            try {
                // Delete anything hanging around here
                toDatabase.delete(Account.TABLE_NAME, null, null);
                toDatabase.delete(HostAuth.TABLE_NAME, null, null);

                // Get our account cursor
                Cursor c = fromDatabase.query(Account.TABLE_NAME, Account.CONTENT_PROJECTION,
                        null, null, null, null, null);
                if (c == null) return 0;
                LogUtils.d(TAG, "fromDatabase accounts: " + c.getCount());
                try {
                    // Loop through accounts, copying them and associated host auth's
                    while (c.moveToNext()) {
                        Account account = new Account();
                        account.restore(c);

                        // Clear security sync key and sync key, as these were specific to the
                        // state of the account, and we've reset that...
                        // Clear policy key so that we can re-establish policies from the server
                        // TODO This is pretty EAS specific, but there's a lot of that around
                        account.mSecuritySyncKey = null;
                        account.mSyncKey = null;
                        account.mPolicyKey = 0;

                        // Copy host auth's and update foreign keys
                        HostAuth hostAuth = restoreHostAuth(fromDatabase,
                                account.mHostAuthKeyRecv);

                        // The account might have gone away, though very unlikely
                        if (hostAuth == null) continue;
                        account.mHostAuthKeyRecv = toDatabase.insert(HostAuth.TABLE_NAME, null,
                                hostAuth.toContentValues());

                        // EAS accounts have no send HostAuth
                        if (account.mHostAuthKeySend > 0) {
                            hostAuth = restoreHostAuth(fromDatabase, account.mHostAuthKeySend);
                            // Belt and suspenders; I can't imagine that this is possible,
                            // since we checked the validity of the account above, and the
                            // database is now locked
                            if (hostAuth == null) continue;
                            account.mHostAuthKeySend = toDatabase.insert(
                                    HostAuth.TABLE_NAME, null, hostAuth.toContentValues());
                        }

                        // Now, create the account in the "to" database
                        toDatabase.insert(Account.TABLE_NAME, null, account.toContentValues());
                        copyCount++;
                    }
                } finally {
                    c.close();
                }

                // Say it's ok to commit
                toDatabase.setTransactionSuccessful();
            } finally {
                toDatabase.endTransaction();
            }
        } catch (SQLiteException ex) {
            LogUtils.w(TAG, "Exception while copying account tables", ex);
            copyCount = -1;
        } finally {
            fromDatabase.endTransaction();
        }
        return copyCount;
    }
--------------------

@Override
        public void onReceive(Context context, Intent intent) {
            updateRunningAccounts();

            // Kick off sync for everyone, since this was a radical account change
            scheduleSync(null, UserHandle.USER_ALL, SyncOperation.REASON_ACCOUNTS_UPDATED, null,
                    null, 0 /* no delay */, 0/* no delay */, false);
        }
--------------------

private static boolean loadsFromServer(Context context, Mailbox m, String protocol) {
        String legacyImapProtocol = context.getString(R.string.protocol_legacy_imap);
        String pop3Protocol = context.getString(R.string.protocol_pop3);
        if (legacyImapProtocol.equals(protocol)) {
            // TODO: actually use a sync flag when creating the mailboxes. Right now we use an
            // approximation for IMAP.
            return m.mType != Mailbox.TYPE_DRAFTS
                    && m.mType != Mailbox.TYPE_OUTBOX
                    && m.mType != Mailbox.TYPE_SEARCH;

        } else if (pop3Protocol.equals(protocol)) {
            return Mailbox.TYPE_INBOX == m.mType;
        }

        return false;
    }
--------------------

