public TailGlobalStep(final Traversal.Admin traversal, final long limit) {
        super(traversal);
        this.limit = limit;
        this.tail = new ArrayDeque<>((int) limit);
    }
--------------------

public static String computeStartLabel(final List<Traversal.Admin<Object, Object>> traversals) {
            {
                // a traversal start label, that's not used as an end label, must be the step's start label
                final Set<String> startLabels = new HashSet<>();
                final Set<String> endLabels = new HashSet<>();
                for (final Traversal.Admin<Object, Object> traversal : traversals) {
                    Helper.getEndLabel(traversal).ifPresent(endLabels::add);
                    startLabels.addAll(Helper.getStartLabels(traversal));
                }
                startLabels.removeAll(endLabels);
                if (!startLabels.isEmpty())
                    return startLabels.iterator().next();
            }
            final List<String> sort = new ArrayList<>();
            for (final Traversal.Admin<Object, Object> traversal : traversals) {
                Helper.getStartLabels(traversal).stream().filter(startLabel -> !sort.contains(startLabel)).forEach(sort::add);
                Helper.getEndLabel(traversal).ifPresent(endLabel -> {
                    if (!sort.contains(endLabel))
                        sort.add(endLabel);
                });
            }
            Collections.sort(sort, (a, b) -> {
                for (final Traversal.Admin<Object, Object> traversal : traversals) {
                    final Optional<String> endLabel = Helper.getEndLabel(traversal);
                    if (endLabel.isPresent()) {
                        final Set<String> startLabels = Helper.getStartLabels(traversal);
                        if (a.equals(endLabel.get()) && startLabels.contains(b))
                            return 1;
                        else if (b.equals(endLabel.get()) && startLabels.contains(a))
                            return -1;
                    }
                }
                return 0;
            });
            return sort.get(0);
        }
--------------------

@Override
    public void processAllStarts() {
        if (this.starts.hasNext())
            super.processAllStarts();
    }
--------------------

