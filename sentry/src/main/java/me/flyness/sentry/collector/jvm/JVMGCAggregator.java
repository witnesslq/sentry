package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public class JVMGCAggregator extends AbstractAggregator {
    private GarbageCollectorMXBean fullGC;
    private GarbageCollectorMXBean youngGC;

    private long lastYoungGCCollectionCount = -1;
    private long lastYoungGCCollectionTime = -1;
    private long lastFullGCCollectionCount = -1;
    private long lastFullGCCollectionTime = -1;

    public JVMGCAggregator() {
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {

            if ("ConcurrentMarkSweep".equals(garbageCollectorMXBean.getName())
                    || "MarkSweepCompact".equals(garbageCollectorMXBean.getName())
                    || "PS MarkSweep".equals(garbageCollectorMXBean.getName())
                    || "G1 Old Generation".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for short pausetimes Old Collector".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for throughput Old Collector".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for deterministic pausetimes Old Collector".equals(garbageCollectorMXBean.getName())) {
                fullGC = garbageCollectorMXBean;
            } else if ("ParNew".equals(garbageCollectorMXBean.getName())
                    || "Copy".equals(garbageCollectorMXBean.getName())
                    || "PS Scavenge".equals(garbageCollectorMXBean.getName())
                    || "G1 Young Generation".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for short pausetimes Young Collector".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for throughput Young Collector".equals(garbageCollectorMXBean.getName())
                    || "Garbage collection optimized for deterministic pausetimes Young Collector".equals(garbageCollectorMXBean.getName())
                    ) {
                youngGC = garbageCollectorMXBean;
            }
        }
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(1);
        Map<String, Object> row = new HashMap<String, Object>(10);
        row.put("youngGCCollectionCount", getYoungGCCollectionCount());
        row.put("youngGCCollectionTime", getYoungGCCollectionTime());
        row.put("fullGCCollectionCount", getFullGCCollectionCount());
        row.put("fullGCCollectionTime", getFullGCCollectionTime());

        row.put("youngGCCollectionCountTotal", getYoungGCCollectionCountTotal());
        row.put("youngGCCollectionTimeTotal", getYoungGCCollectionTimeTotal());

        row.put("fullGCCollectionCountTotal", getFullGCCollectionCountTotal());
        row.put("fullGCCollectionTimeTotal", getFullGCCollectionTimeTotal());
        if (fullGC != null) {
            row.put("fullGCMBeanName", fullGC.getName());
        }
        if (youngGC != null) {
            row.put("youngGCMBeanName", youngGC.getName());
        }

        results.add(row);

        return results;
    }

    public String getName() {
        return "gc";
    }

    public long getYoungGCCollectionCountTotal() {
        if (youngGC == null) {
            return 0;
        }
        return youngGC.getCollectionCount();

    }


    public long getYoungGCCollectionTimeTotal() {
        if (youngGC == null) {
            return 0;
        }
        return youngGC.getCollectionTime();
    }


    public long getFullGCCollectionCountTotal() {
        if (fullGC == null) {
            return 0;
        }
        return fullGC.getCollectionCount();
    }


    public long getFullGCCollectionTimeTotal() {
        if (fullGC == null) {
            return 0;
        }
        return fullGC.getCollectionTime();
    }


    public long getYoungGCCollectionCount() {
        long current = getYoungGCCollectionCountTotal();
        if (lastYoungGCCollectionCount == -1) {
            lastYoungGCCollectionCount = current;
            return current;
        } else {
            long reslut = current - lastYoungGCCollectionCount;
            lastYoungGCCollectionCount = current;
            return reslut;
        }

    }


    public long getYoungGCCollectionTime() {
        long current = getYoungGCCollectionTimeTotal();
        if (lastYoungGCCollectionTime == -1) {
            lastYoungGCCollectionTime = current;
            return current;
        } else {
            long reslut = current - lastYoungGCCollectionTime;
            lastYoungGCCollectionTime = current;
            return reslut;
        }
    }


    public long getFullGCCollectionCount() {
        long current = getFullGCCollectionCountTotal();
        if (lastFullGCCollectionCount == -1) {
            lastFullGCCollectionCount = current;
            return current;
        } else {
            long reslut = current - lastFullGCCollectionCount;
            lastFullGCCollectionCount = current;
            return reslut;
        }
    }


    public long getFullGCCollectionTime() {
        long current = getFullGCCollectionTimeTotal();
        if (lastFullGCCollectionTime == -1) {
            lastFullGCCollectionTime = current;
            return current;
        } else {
            long reslut = current - lastFullGCCollectionTime;
            lastFullGCCollectionTime = current;
            return reslut;
        }
    }

    public static void main(String[] args) {
        JVMGCAggregator jvmgcAggregator = new JVMGCAggregator();
        jvmgcAggregator.print();
    }
}
