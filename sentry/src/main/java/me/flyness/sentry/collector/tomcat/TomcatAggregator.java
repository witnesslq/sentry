package me.flyness.sentry.collector.tomcat;

import me.flyness.sentry.collector.AbstractAggregator;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bjlizhitao on 2016/9/26.
 */
public class TomcatAggregator extends AbstractAggregator {
    private MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    /*
     * 数据没有采集到的次数
     */
    private int isNullTimes = 0;

    private ObjectName httpObjectName = null;

    private AtomicInteger currentThreadsBusyMax = new AtomicInteger(0);

    public List<Map<String, Object>> harvest() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        if (isNullTimes > 3) {
            return null;
        }

        try {
            if (httpObjectName == null) {
                ObjectName objName = new ObjectName("Catalina:type=ThreadPool,name=*");
                Set<ObjectName> nn = server.queryNames(objName, null);

                if (nn == null || nn.isEmpty()) {
                    isNullTimes++;
                    return null;
                } else {

                    for (ObjectName on : nn) { //找到第一个
                        String name = on.getKeyProperty("name");
                        if (name != null && name.contains("http")) {// 对于tomcat不一样的版本名字不一样，不过都带http
                            httpObjectName = on;
                            break;
                        }
                    }
                }

                if (httpObjectName == null) {
                    isNullTimes++;
                    return null;
                }
            }

            Map<String, Object> row = new HashMap<String, Object>();

            Object o = server.getAttribute(httpObjectName, "currentThreadsBusy");
            if (o != null) {
                int i = Integer.parseInt(o.toString());
                row.put("currentThreadsBusy", i);
                int max = currentThreadsBusyMax.getAndSet(0);
                if (i > max) {
                    row.put("currentThreadsBusyMax", i);
                } else {
                    row.put("currentThreadsBusyMax", max);
                }

            }
            o = server.getAttribute(httpObjectName, "currentThreadCount");
            if (o != null) {
                row.put("currentThreadCount", Integer.parseInt(o.toString()));
            }

            o = server.getAttribute(httpObjectName, "maxThreads");
            if (o != null) {
                row.put("maxThreads", Integer.parseInt(o.toString()));
            }
            if (row.isEmpty()) {
            }

            items.add(row);
            return items;
        } catch (Exception e) {
        }


        return null;
    }

    public String getName() {
        return "tomcat";
    }
}
