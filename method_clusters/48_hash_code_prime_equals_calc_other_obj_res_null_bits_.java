@Override
            public int hashCode() {
                int result = action != null ? action.hashCode() : 0;
                result = 31 * result + (int) (delay ^ (delay >>> 32));
                return result;
            }
--------------------

@Override
    public int hashCode() {
        int result = nresults;
        result = 31 * result + queryTime;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }
--------------------

@Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < offset; i++) {
            hash = hash + set[i].hashCode();
        }
        return getCredentialClass().hashCode() + hash;
    }
--------------------

