package com.example.devutils.utils.system;

import com.example.devutils.utils.text.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-10-26 16:55.
 */
public class SystemEnvUtils {

    public static String getPathString() {
        return System.getenv("path");
    }

    public static List<String> getPathList() {
        String pathString = getPathString();
        String pathSeparator = SystemPropertyUtils.getPathSeparator();
        return Arrays.stream(pathString.split(pathSeparator)).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
