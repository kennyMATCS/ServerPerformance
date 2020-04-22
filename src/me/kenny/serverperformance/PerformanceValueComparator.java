package me.kenny.serverperformance;

import java.util.Comparator;

class PeformanceValueComparator implements Comparator<PerformanceValue> {
    @Override
    public int compare(PerformanceValue x, PerformanceValue y) {
        long xTicks = x.getTicks();
        long yTicks = y.getTicks();
        return compare(xTicks, yTicks);
    }

    private static int compare(long a, long b) {
        return a > b ? -1
                : a < b ? 1
                : 0;
    }
}