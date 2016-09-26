package me.flyness.sentry.javaagent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * Created by bjlizhitao on 2016/9/14.
 */
public class TestAgent {
    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        String premainJarPath = Premain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(premainJarPath);

        File jarFile = new File(premainJarPath);
        System.out.println(jarFile.getParentFile().getPath());

        Class<?> initClass = Class.forName("cc.fly.sentry.SentryInitializer");

        Method method = initClass.getMethod("initSentry", new Class[]{String.class, Instrumentation.class});

        Object initObject = initClass.newInstance();
        method.invoke(initObject, new Object[]{agentArgs, inst});
    }
}
