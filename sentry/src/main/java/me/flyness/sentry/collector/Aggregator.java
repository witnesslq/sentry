package me.flyness.sentry.collector;

import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2016/9/7.
 */
public interface Aggregator {
    List<Map<String, Object>> harvest();
    String getName();
    void print();
}
