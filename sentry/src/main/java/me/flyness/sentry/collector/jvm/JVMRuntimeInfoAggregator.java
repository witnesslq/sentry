package me.flyness.sentry.collector.jvm;

import me.flyness.sentry.collector.AbstractAggregator;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/12.
 */
public class JVMRuntimeInfoAggregator extends AbstractAggregator {
    private RuntimeMXBean runtimeMXBean;

    public JVMRuntimeInfoAggregator() {
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        // 虚拟机启动参数
        List<String> inputArguments = runtimeMXBean.getInputArguments();
        Map<String, Object> row = new HashMap<String, Object>(6);
        // pid
        row.put("pid", getPid());
        // 虚拟机启动时间
        row.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(runtimeMXBean.getStartTime()));
        // 虚拟机名称
        row.put("vmName", runtimeMXBean.getVmName());
        // 虚拟机供应商
        row.put("vmVendor", runtimeMXBean.getVmVendor());
        // 启动参数
        row.put("inputArguments", inputArguments);
        // javaHome
        row.put("javaHome", System.getProperty("java.home"));

        items.add(row);

        return items;
    }

    /**
     * 获取虚拟机pid
     * @return
     */
    private String getPid() {
        return runtimeMXBean.getName().split("@")[0];
    }

    public String getName() {
        return "JVM info";
    }

    public static void main(String[] args) {
        new JVMRuntimeInfoAggregator().print();
    }
}
