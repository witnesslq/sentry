package me.flyness.sentry.test;

import java.util.Properties;

/**
 * Created by bjlizhitao on 2016/9/14.
 */
public class SentryTest {
    public void sayHello() {
        long startTime = System.currentTimeMillis();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");

        long endTime = System.currentTimeMillis();
        System.out.println("方法耗时为：" + (endTime - startTime) + "ms");
    }

    public void sayHello2() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is sayHello2 method");
    }

    public static void main(String[] args) {
        SentryTest sentryTest = new SentryTest();
        sentryTest.sayHello();
        System.out.println();
        sentryTest.sayHello2();

        Properties properties = System.getProperties();

        properties.list(System.out);
    }
}
