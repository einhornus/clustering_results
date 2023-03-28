@Override
        public void search(IIndexCursor cursor, ISearchPredicate searchPred) throws HyracksDataException {
            if (searcher == null) {
                searcher = new PartitionedTOccurrenceSearcher(index, ctx);
            }
            searcher.search(cursor, (InvertedIndexSearchPredicate) searchPred, opCtx);
        }
--------------------

public TrackingFooter(String f, final String m, final String s)
      throws PatternSyntaxException {
    f = f.trim();
    if (f.endsWith(":")) {
      f = f.substring(0, f.length() - 1);
    }
    this.key = new FooterKey(f);
    this.match = Pattern.compile(m.trim());
    this.system = s.trim();
  }
--------------------

public FrameFixedFieldTupleAppender(int numFields) {
        tupleAppender = new FrameTupleAppender();
        fieldAppender = new FrameFixedFieldAppender(numFields);
        lastAppender = tupleAppender;
    }
--------------------

