package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public class JVMMemoryPoolAggregator extends AbstractAggregator {
    private List<MemoryPoolMXBean> memoryPoolMXBeans;

    public JVMMemoryPoolAggregator() {
        memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        Map<String, Object> row = new HashMap<String, Object>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans){
            row.put(memoryPoolMXBean.getName() + " init", memoryPoolMXBean.getUsage().getInit());
            row.put(memoryPoolMXBean.getName() + " committed", memoryPoolMXBean.getUsage().getCommitted());
            row.put(memoryPoolMXBean.getName() + " used", memoryPoolMXBean.getUsage().getUsed());
            row.put(memoryPoolMXBean.getName() + " max", memoryPoolMXBean.getUsage().getMax());
        }

        items.add(row);
        return items;
    }

    public String getName() {
        return "memory pool";
    }

    public static void main(String[] args) {
        new JVMMemoryPoolAggregator().print();
    }
}
