package com.example.devutils.utils.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AMe on 2020-06-16 18:30.
 */
public class RegexUtils {

    public static Pattern getPattern(String regex, boolean ignoreCase) {
        return Pattern.compile(regex, ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
    }

    public static boolean match(String text, String regex, boolean ignoreCase) {
        Pattern pattern = getPattern(regex, ignoreCase);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static String[] findOne(String text, String regex, boolean ignoreCase) {
        Pattern pattern = getPattern(regex, ignoreCase);
        Matcher matcher = pattern.matcher(text);
        int groupCount = matcher.groupCount();
        if (matcher.find()) {
            String[] itemArr = new String[groupCount + 1];
            for (int i = 0; i <= groupCount; i++) {
                itemArr[i] = matcher.group(i);
            }
            return itemArr;
        }
        return new String[0];
    }

    public static List<String[]> findAll(String text, String regex, boolean ignoreCase) {
        ArrayList<String[]> list = new ArrayList<>();
        Pattern pattern = getPattern(regex, ignoreCase);
        Matcher matcher = pattern.matcher(text);
        int groupCount = matcher.groupCount();
        while (matcher.find()) {
            String[] itemArr = new String[groupCount + 1];
            for (int i = 0; i <= groupCount; i++) {
                itemArr[i] = matcher.group(i);
            }
            list.add(itemArr);
        }
        return list;
    }

}
