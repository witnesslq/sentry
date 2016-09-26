package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public class JVMMemoryAggregator extends AbstractAggregator {
    private MemoryMXBean memoryMXBean;

    public JVMMemoryAggregator() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("heapMemoryUsageInit", memoryMXBean.getHeapMemoryUsage().getInit());
        row.put("heapMemoryUsageCommitted", memoryMXBean.getHeapMemoryUsage().getCommitted());
        row.put("heapMemoryUsageUsed", memoryMXBean.getHeapMemoryUsage().getUsed());
        row.put("heapMemoryUsageMax", memoryMXBean.getHeapMemoryUsage().getMax());

        row.put("nonHeapMemoryUsageInit", memoryMXBean.getNonHeapMemoryUsage().getInit());
        row.put("nonHeapMemoryUsageCommitted", memoryMXBean.getNonHeapMemoryUsage().getCommitted());
        row.put("nonHeapMemoryUsageUsed", memoryMXBean.getNonHeapMemoryUsage().getUsed());
        row.put("nonHeapMemoryUsageMax", memoryMXBean.getNonHeapMemoryUsage().getMax());

        row.put("objectPendingFinalizationCount", memoryMXBean.getObjectPendingFinalizationCount());
        ll.add(row);

        return ll;
    }

    public String getName() {
        return "memory";
    }

    public static void main(String[] args) {
        JVMMemoryAggregator jvmMemoryAggregator = new JVMMemoryAggregator();
        jvmMemoryAggregator.print();
    }
}
