package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public class JVMClassLoadingAggregator extends AbstractAggregator {
    private ClassLoadingMXBean classLoadingMXBean;

    public JVMClassLoadingAggregator() {
        classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(1);
        Map<String, Object> row = new HashMap<String, Object>(3);
        row.put("loadedClassCount", classLoadingMXBean.getLoadedClassCount());
        row.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        row.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());

        results.add(row);

        return results;
    }

    public String getName() {
        return "classLoading";
    }

    public static void main(String[] args) throws InterruptedException {
        JVMClassLoadingAggregator jvmClassLoadingAggregator = new JVMClassLoadingAggregator();
        jvmClassLoadingAggregator.print();
    }
}
