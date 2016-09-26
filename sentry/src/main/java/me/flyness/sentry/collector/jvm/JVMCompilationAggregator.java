package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/12.
 */
public class JVMCompilationAggregator extends AbstractAggregator {
    private CompilationMXBean compilationMXBean;

    public JVMCompilationAggregator(){
        compilationMXBean = ManagementFactory.getCompilationMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("name", compilationMXBean.getName());
        row.put("totalCompilationTime", compilationMXBean.getTotalCompilationTime());

        items.add(row);

        return items;
    }

    public String getName() {
        return "compilation";
    }

    public static void main(String[] args) {
        new JVMCompilationAggregator().print();
    }
}
