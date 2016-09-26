package me.flyness.sentry.collector.os;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/12.
 */
public class OSAggregator extends AbstractAggregator {
    private OperatingSystemMXBean operatingSystemMXBean;

    public OSAggregator(){
        operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("arch", operatingSystemMXBean.getArch());
        row.put("name", operatingSystemMXBean.getName());
        row.put("version", operatingSystemMXBean.getVersion());
        row.put("availableProcessors", operatingSystemMXBean.getAvailableProcessors());
        row.put("systemLoadAverage", operatingSystemMXBean.getSystemLoadAverage());

        items.add(row);

        return items;
    }

    public String getName() {
        return "os";
    }

    public static void main(String[] args) {
        new OSAggregator().print();
    }
}
