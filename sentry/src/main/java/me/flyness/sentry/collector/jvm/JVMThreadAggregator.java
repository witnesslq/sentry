package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public class JVMThreadAggregator extends AbstractAggregator {
    private ThreadMXBean threadMXBean;

    public JVMThreadAggregator() {
        threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(1);
        Map<String, Object> row = new HashMap<String, Object>(3);
        row.put("threadCount", threadMXBean.getThreadCount());
        row.put("peakThreadCount", threadMXBean.getPeakThreadCount());
        threadMXBean.resetPeakThreadCount();//重新设置峰值线程数

        row.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
        row.put("deadlockedThreadsCount", threadMXBean.findMonitorDeadlockedThreads() == null ? 0 : threadMXBean.findMonitorDeadlockedThreads().length);
        row.put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());

        results.add(row);

        return results;
    }

    public String getName() {
        return "thread";
    }

    public static void main(String[] args) throws InterruptedException {
        JVMThreadAggregator jvmThreadAggregator = new JVMThreadAggregator();
        jvmThreadAggregator.print();
    }
}
