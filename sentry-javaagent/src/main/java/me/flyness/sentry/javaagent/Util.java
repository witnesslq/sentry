package me.flyness.sentry.javaagent;

import java.util.List;

public class Util {
    public static String getHighestVersionCollector(List<String> list) {
        String s = null;
        for (String path : list) {
            if (s == null) {
                s = path;
            } else if (isHigher(path, s)) {
                s = path;
            }
        }
        return s;
    }

    private static boolean isHigher(String a, String b) {
        String version1 = a.substring(Premain.COLLECTOR_PREFIX.length(), a.length() - Premain.COLLECTOR_SUFFIX.length());
        String version2 = b.substring(Premain.COLLECTOR_PREFIX.length(), b.length() - Premain.COLLECTOR_SUFFIX.length());
        int[] iv1 = splitVersion(version1);
        int[] iv2 = splitVersion(version2);
        for (int i = 0; i < 3; i++) {
            if (iv1[i] > iv2[i]) {
                return true;
            }
        }
        return false;
    }

    private static int[] splitVersion(String v) {
        String[] vv = v.split("\\.");
        int[] iv = new int[3];
        iv[0] = Integer.parseInt(vv[0]);
        iv[1] = Integer.parseInt(vv[1]);
        iv[2] = Integer.parseInt(vv[2]);
        return iv;
    }

    public static void main(String[] args) {
        String s1 = "0.1.0";
        String s2 = "0.3.4";
    }
}
