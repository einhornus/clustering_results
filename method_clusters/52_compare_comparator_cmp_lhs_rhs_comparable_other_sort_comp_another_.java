@Override
            public int compare(Rank o1, Rank o2)
            {
                return o1.rank - o2.rank;
            }
--------------------

public int compareTo(Entry other) {
            if (handler < other.handler) {
                return -1;
            } else if (handler > other.handler) {
                return 1;
            }

            return exceptionType.compareTo(other.exceptionType);
        }
--------------------

@Override
    public int compareTo(HLLCounterOld o) {
        if (o == null)
            return 1;

        long e1 = this.getCountEstimate();
        long e2 = o.getCountEstimate();

        if (e1 == e2)
            return 0;
        else if (e1 > e2)
            return 1;
        else
            return -1;
    }
--------------------

