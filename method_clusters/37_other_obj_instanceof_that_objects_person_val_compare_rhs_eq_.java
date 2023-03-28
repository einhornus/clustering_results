@Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof FormField))
            return false;

        FormField other = (FormField) obj;

        return toXML().equals(other.toXML());
    }
--------------------

@Override
        public boolean equals(Object other) {
            if (!(other instanceof NotificationKey)) {
                return false;
            }
            NotificationKey key = (NotificationKey) other;
            return account.getAccountManagerAccount().equals(key.account.getAccountManagerAccount())
                    && folder.equals(key.folder);
        }
--------------------

@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Uid uid = (Uid) o;

        if (id != null ? !id.equals(uid.id) : uid.id != null) return false;
        if (type != null ? !type.equals(uid.type) : uid.type != null) return false;

        return true;
    }
--------------------

