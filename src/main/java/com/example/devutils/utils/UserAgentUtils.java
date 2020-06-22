package com.example.devutils.utils;

import com.example.devutils.dep.Charsets;
import com.example.devutils.dep.UserAgents;
import com.example.devutils.utils.collection.MapUtils;
import com.example.devutils.utils.io.StreamUtils;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-21 17:55.
 */
public class UserAgentUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserAgentUtils.class);

    private static class UserAgentsHolder {
        private static final UserAgents userAgents = new UserAgents();
        private static final HashMap<String, String> hashMap = MapUtils.of(HashMap::new,
            "ua_android.txt", "android",
            "ua_pc_chrome.txt", "pc_chrome",
            "ua_pc_firefox.txt", "pc_firefox"
        );

        static {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                String fileName = entry.getKey();
                String group = entry.getValue();
                logger.info("加载User-Agent文件: {}", fileName);
                try (
                    InputStream inputStream = UserAgentUtils.class.getClassLoader().getResourceAsStream(fileName);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ) {
                    List<String> lineList = StreamUtils.readAsLines(bufferedReader);
                    userAgents.add(lineList, group);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String next() {
        return UserAgentsHolder.userAgents.next();
    }

    public static String next(String group) {
        return UserAgentsHolder.userAgents.next(group);
    }

    public static String random() {
        return UserAgentsHolder.userAgents.random();
    }

    public static String random(String group) {
        return UserAgentsHolder.userAgents.random(group);
    }

    public static UserAgent parseUserAgent(String userAgent) {
        return UserAgent.parseUserAgentString(userAgent);
    }

}
