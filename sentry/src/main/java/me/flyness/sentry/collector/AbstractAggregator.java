package me.flyness.sentry.collector;

import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public abstract class AbstractAggregator implements Aggregator {
    public void print() {
        List<Map<String, Object>> results = harvest();

        for (Map<String, Object> result : results) {
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }
}
