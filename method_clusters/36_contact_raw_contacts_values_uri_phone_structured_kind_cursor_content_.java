public void testQueryContactWithStatusUpdate() {
        ContentValues values = new ContentValues();
        long contactId = createContact(values, "John", "Doe",
                "18004664411", "goog411@acme.com", StatusUpdates.INVISIBLE, 4, 1, 0,
                StatusUpdates.CAPABILITY_HAS_CAMERA);
        values.put(Contacts.CONTACT_PRESENCE, StatusUpdates.INVISIBLE);
        values.put(Contacts.CONTACT_CHAT_CAPABILITY, StatusUpdates.CAPABILITY_HAS_CAMERA);
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
        assertStoredValuesWithProjection(contactUri, values);
        assertSelectionWithProjection(Contacts.CONTENT_URI, values, Contacts._ID, contactId);
    }
--------------------

private void updateCustomContactVisibility(SQLiteDatabase db, long optionalContactId) {
        final long groupMembershipMimetypeId = getMimeTypeId(GroupMembership.CONTENT_ITEM_TYPE);
        String[] selectionArgs = new String[]{String.valueOf(groupMembershipMimetypeId)};

        final String contactIdSelect = (optionalContactId < 0) ? "" :
                (Contacts._ID + "=" + optionalContactId + " AND ");

        // First delete what needs to be deleted, then insert what needs to be added.
        // Since flash writes are very expensive, this approach is much better than
        // delete-all-insert-all.
        db.execSQL(
                "DELETE FROM " + Tables.VISIBLE_CONTACTS +
                " WHERE " + Contacts._ID + " IN" +
                    "(SELECT " + Contacts._ID +
                    " FROM " + Tables.CONTACTS +
                    " WHERE " + contactIdSelect + "(" + Clauses.CONTACT_IS_VISIBLE + ")=0) ",
                selectionArgs);

        db.execSQL(
                "INSERT INTO " + Tables.VISIBLE_CONTACTS +
                " SELECT " + Contacts._ID +
                " FROM " + Tables.CONTACTS +
                " WHERE " +
                    contactIdSelect +
                    Contacts._ID + " NOT IN " + Tables.VISIBLE_CONTACTS +
                    " AND (" + Clauses.CONTACT_IS_VISIBLE + ")=1 ",
                selectionArgs);
    }
--------------------

private void expectContactEntityQuery(String lookupKey, int contactId) {
        Uri uri = Uri.withAppendedPath(
                Contacts.getLookupUri(contactId, lookupKey), Contacts.Entity.CONTENT_DIRECTORY);
        ContentValues row1 = new ContentValues();
        row1.put(Contacts.Entity.DATA_ID, 1);
        row1.put(Contacts.Entity.LOOKUP_KEY, lookupKey);
        row1.put(Contacts.Entity.CONTACT_ID, contactId);
        row1.put(Contacts.Entity.DISPLAY_NAME, "Contact " + contactId);
        row1.put(Contacts.Entity.ACCOUNT_NAME, TEST_ACCOUNT);
        row1.put(Contacts.Entity.ACCOUNT_TYPE, TEST_ACCOUNT_TYPE);
        mContactsProvider
                .expectQuery(uri)
                .withAnyProjection()
                .withAnySortOrder()
                .returnRow(row1)
                .anyNumberOfTimes();
    }
--------------------

