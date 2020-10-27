package com.example.devutils.utils.system;

/**
 * Created by AMe on 2020-10-26 16:35.
 */
public class SystemPropertyUtils {

    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    public static String getUserCountry() {
        return System.getProperty("user.country");
    }

    public static String getIoTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getUserTimeZone() {
        return System.getProperty("user.timezone");
    }

    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static String getUserLanguage() {
        return System.getProperty("user.language");
    }

    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }
}
