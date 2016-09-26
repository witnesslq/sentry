package me.flyness.sentry.collector.os;

import me.flyness.sentry.collector.AbstractAggregator;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

/**
 * Created by bjlizhitao on 2016/9/12.
 */
public class CPUAggregator extends AbstractAggregator {
    private OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    private MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    private ObjectName operatingSystemBean = null;

    private int processorCount = 1;

    private Long lastProcessCpuTime = null;//纳秒
    private Long lastSystemTime = null;

    public CPUAggregator() {
        try {
            processorCount = osBean.getAvailableProcessors();
            ObjectName objName = new ObjectName("java.lang:type=OperatingSystem");
            Set<ObjectName> nn = server.queryNames(objName, null);
            if (nn != null && !nn.isEmpty()) {
                for (ObjectName on : nn) { //找到第一个
                    operatingSystemBean = on;
                }

            }
        } catch (Exception e) {
        }

        if (operatingSystemBean == null) {
        }

        lastProcessCpuTime = getProcessCpuTime();
        lastSystemTime = System.nanoTime();
    }

    private Long getProcessCpuTime() {
        if (operatingSystemBean == null) {
            return null;
        }

        try {
            Object o = server.getAttribute(operatingSystemBean, "ProcessCpuTime");
            if (o != null) {
                try {
                    long v = Long.parseLong(o.toString());
                    return v;
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        Long currentProcessTime = getProcessCpuTime();
        if (currentProcessTime == null || lastProcessCpuTime == null) {
            return null;
        }
        long currentSystemTime = System.nanoTime();
        long cpuTimeInterval = currentProcessTime - lastProcessCpuTime;
        long sysTimeInterval = currentSystemTime - lastSystemTime;
        double ratio = ((double) cpuTimeInterval / sysTimeInterval) * 100 / processorCount;

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("cpuTimeInterval", cpuTimeInterval);
        row.put("totalCpuTime", currentProcessTime);
        row.put("processorCount", processorCount);
        row.put("systemTimeInterval", sysTimeInterval);
        row.put("cpuRatio", ratio);

        this.lastProcessCpuTime = currentProcessTime;
        this.lastSystemTime = currentSystemTime;

        items.add(row);

        return items;
    }

    public String getName() {
        return "cpu";
    }

    public static void main(String[] args) {
        new CPUAggregator().print();
    }
}
