package me.flyness.sentry.javaagent;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by bjlizhitao on 2016/9/13.
 * 监控系统日志工厂
 */
public class SentryLoggerFactory {
    public static int FILE_COUNT = 3;
    public static int FILE_SIZE = 52428800;

    public static FileHandler buildFileHandler(String logRoot, String appName, String instance) {
        String homePath = null;
        if (logRoot != null) {
            File f = new File(logRoot.trim());
            if ((f.isDirectory()) && (f.canWrite())) {
                homePath = logRoot;
            }
        }
        if (homePath == null) {
            homePath = (String) System.getProperties().get("user.home");
            if (homePath == null) {
                homePath = "/tmp";
            }
        }
        String folder = homePath + File.separator + "sentry-javaagent";
        String filepath;
        if (appName == null) {
            filepath = folder + File.separator + "error.log";
        } else {
            if ((instance == null) || (instance.equals("default"))) {
                filepath = folder + File.separator + "sentry_javaagent_" + appName + ".log";
            } else {
                filepath = folder + File.separator + "sentry_javaagent_" + appName + "-" + instance + ".log";
            }
        }
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        try {
            FileHandler fileHandler = new FileHandler(filepath, FILE_SIZE, FILE_COUNT);
            fileHandler.setFormatter(new SimpleFormatter());
            return fileHandler;
        } catch (Exception e) {
            throw new RuntimeException("failed to init fileHandler,filepath:" + filepath);
        }
    }

    public static Logger getLogger(Class<?> c) {
        Logger logger = Logger.getLogger(c.getName());
        logger.setUseParentHandlers(false);

        return logger;
    }
}
