public boolean signIn(User u) {
		if (u == null) {
			return false;
		}
		setUser(u, null);
		return true;
	}
--------------------

private static void checkEmptyGroup(String prefix, User u) {
		assertNotNull(u, prefix + ":: Created user should be available");
		assertNotNull(u.getGroupUsers(), prefix + ":: List<GroupUser> for newly created user should not be null");
		assertTrue(u.getGroupUsers().isEmpty(), prefix + ":: List<GroupUser> for newly created user should be empty");
	}
--------------------

private String getPassword() throws HsqlException {

        String token = tokenizer.getString();

        return token.toUpperCase(Locale.ENGLISH);
    }
--------------------

