public static Range findRangeBounds(XYDataset dataset,
            List visibleSeriesKeys, Range xRange, boolean includeInterval) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        Range result = null;
        if (dataset instanceof XYRangeInfo) {
            XYRangeInfo info = (XYRangeInfo) dataset;
            result = info.getRangeBounds(visibleSeriesKeys, xRange,
                    includeInterval);
        }
        else {
            result = iterateToFindRangeBounds(dataset, visibleSeriesKeys,
                    xRange, includeInterval);
        }
        return result;
    }
--------------------

public double getCategorySeriesMiddle(int categoryIndex, int categoryCount,
            int seriesIndex, int seriesCount, double itemMargin,
            Rectangle2D area, RectangleEdge edge) {

        double start = getCategoryStart(categoryIndex, categoryCount, area,
                edge);
        double end = getCategoryEnd(categoryIndex, categoryCount, area, edge);
        double width = end - start;
        if (seriesCount == 1) {
            return start + width / 2.0;
        }
        else {
            double gap = (width * itemMargin) / (seriesCount - 1);
            double ww = (width * (1 - itemMargin)) / seriesCount;
            return start + (seriesIndex * (ww + gap)) + ww / 2.0;
        }
    }
--------------------

public Object clone() throws CloneNotSupportedException {
        // TODO: I think we need to make sure the keys are actually cloned,
        // whereas the paint instances are always immutable so they're OK
        return super.clone();
    }
--------------------

