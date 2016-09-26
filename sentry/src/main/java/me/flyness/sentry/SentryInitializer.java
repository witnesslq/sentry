package me.flyness.sentry;

import me.flyness.sentry.transformer.MethodTransformer;

import java.lang.instrument.Instrumentation;

/**
 * Created by bjlizhitao on 2016/9/14.
 */
public class SentryInitializer {
    public void initSentry(String agentArgs, Instrumentation instrumentation){
        String premainJarPath = SentryInitializer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("premainJarPath: " + premainJarPath);
        System.out.println("agentArgs: " + agentArgs);
        instrumentation.addTransformer(new MethodTransformer());
    }
}
